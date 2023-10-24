package com.javaweb.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.javaweb.admin.constant.GoutongConstant;
import com.javaweb.admin.entity.Goutong;
import com.javaweb.admin.entity.Tandian;
import com.javaweb.admin.mapper.GoutongMapper;
import com.javaweb.admin.mapper.TandianMapper;
import com.javaweb.admin.websocket.WebSocketMessage;
import com.javaweb.admin.websocket.WebSocketServer;
import com.javaweb.common.common.BaseQuery;
import com.javaweb.common.config.UploadFileConfig;
import com.javaweb.common.service.IUploadService;
import com.javaweb.common.utils.DateUtils;
import com.javaweb.common.utils.easypoi.ExcelBaseParam;
import com.javaweb.common.utils.easypoi.ExcelDownloadUtil;
import com.javaweb.common.utils.easypoi.ExcelSimpleUtil;
import com.javaweb.system.common.BaseServiceImpl;
import com.javaweb.common.config.CommonConfig;
import com.javaweb.common.utils.CommonUtils;
import com.javaweb.common.utils.JsonResult;
import com.javaweb.common.utils.StringUtils;
import com.javaweb.admin.constant.CustomerConstant;
import com.javaweb.admin.entity.Customer;
import com.javaweb.admin.mapper.CustomerMapper;
import com.javaweb.admin.query.CustomerQuery;
import com.javaweb.admin.service.ICustomerService;
import com.javaweb.system.utils.AdminUtils;
import com.javaweb.admin.vo.CustomerListVo;
import com.javaweb.system.utils.ShiroUtils;
import com.sun.org.apache.bcel.internal.generic.GOTO;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 客户信息 服务实现类
 * </p>
 *
 * @author leavin
 * @since 2023-03-31
 */
@Service
public class CustomerServiceImpl extends BaseServiceImpl<CustomerMapper, Customer> implements ICustomerService {
    private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private IUploadService uploadService;

    @Autowired
    private UploadFileConfig uploadFileConfig;

    @Autowired
    private TandianMapper tandianMapper;

    @Autowired
    private GoutongMapper goutongMapper;

    /**
     * 获取数据列表
     *
     * @param query 查询条件
     * @return
     */
    @Override
    public JsonResult getList(BaseQuery query) {
        CustomerQuery customerQuery = (CustomerQuery) query;
        // 查询条件
        LambdaQueryWrapper<Customer> queryWrapper = new LambdaQueryWrapper<>();
        if(StringUtils.isNotEmpty(customerQuery.getTandianFlag())){
            //是否有探店
            if(StringUtils.equals("1",customerQuery.getTandianFlag())){
                queryWrapper.eq(Customer::getTdNum,0);
            }else if(StringUtils.equals("2",customerQuery.getTandianFlag())){
                queryWrapper.eq(Customer::getTdNum,1);
            }else if(StringUtils.equals("3",customerQuery.getTandianFlag())){
                queryWrapper.gt(Customer::getTdNum,1);
            }
        }
        if(customerQuery.getUserId()!=null&&customerQuery.getUserId()>0){
            queryWrapper.eq(Customer::getCreateUser,customerQuery.getUserId());
        }
        if(StringUtils.isNotEmpty(customerQuery.getInteractTimeStr())){
            //下次沟通时间
            queryWrapper.apply(" id in (select g.cust_id from sys_goutong g inner join (select max(id) id,cust_id from sys_goutong group by cust_id) t where g.id = t.id and DATE_FORMAT(g.interact_time,'%Y-%m-%d')='"+customerQuery.getInteractTimeStr()+"')");
        }
        if(StringUtils.isNotEmpty(customerQuery.getGtTimeStrStart())&&StringUtils.isEmpty(customerQuery.getGtTimeStrEnd())){
            //沟通时间
            queryWrapper.apply(" id in (select cust_id from sys_goutong where DATE_FORMAT(gt_time,'%Y-%m-%d')>='"+customerQuery.getGtTimeStrStart()+"')");
        }else if(StringUtils.isEmpty(customerQuery.getGtTimeStrStart())&&StringUtils.isNotEmpty(customerQuery.getGtTimeStrEnd())){
            //沟通时间
            queryWrapper.apply(" id in (select cust_id from sys_goutong where DATE_FORMAT(gt_time,'%Y-%m-%d')<='"+customerQuery.getGtTimeStrEnd()+"')");
        }else if(StringUtils.isNotEmpty(customerQuery.getGtTimeStrStart())&&StringUtils.isNotEmpty(customerQuery.getGtTimeStrEnd())){
            //沟通时间
            queryWrapper.apply(" id in (select cust_id from sys_goutong where DATE_FORMAT(gt_time,'%Y-%m-%d')>='"+customerQuery.getGtTimeStrStart()+"' and DATE_FORMAT(gt_time,'%Y-%m-%d')<='"+customerQuery.getGtTimeStrEnd()+"')");
        }
        if(StringUtils.isNotEmpty(customerQuery.getReplyFlag())){
            queryWrapper.apply(" id in (select g.cust_id from sys_goutong g inner join (select max(id) id,cust_id from sys_goutong group by cust_id) t where g.id = t.id and  g.reply_flag="+customerQuery.getReplyFlag()+")");
        }
        if(StringUtils.isNotEmpty(customerQuery.getInviteTimeStr())) {
            //邀约时间
            queryWrapper.apply(" DATE_FORMAT(invite_time,'%Y-%m-%d')='"+customerQuery.getInviteTimeStr()+"'");
        }

        if(StringUtils.isNotEmpty(customerQuery.getCreateTimeStrStart())) {
            //客资录入开始时间
            queryWrapper.apply(" DATE_FORMAT(create_time,'%Y-%m-%d')>='"+customerQuery.getCreateTimeStrStart()+"'");
        }

        if(StringUtils.isNotEmpty(customerQuery.getCreateTimeStrEnd())) {
            //客资录入开始时间
            queryWrapper.apply(" DATE_FORMAT(create_time,'%Y-%m-%d')<='"+customerQuery.getCreateTimeStrEnd()+"'");
        }

        // 姓名/昵称
        if (!StringUtils.isEmpty(customerQuery.getKeywords())) {
            queryWrapper.and(wrapper->wrapper.like(Customer::getCustName, customerQuery.getKeywords()).or().like(Customer::getNickName, customerQuery.getKeywords()));
        }

        // 性别：1=男,2=女,3=未知
        if (!StringUtils.isEmpty(customerQuery.getSex())) {
            queryWrapper.eq(Customer::getSex, customerQuery.getSex());
        }
        // 客户类型： 1=A,2=B,3=C,4=D,5=E,6=S,7=成交,8=F
        if (!StringUtils.isEmpty(customerQuery.getCustType())) {
            queryWrapper.eq(Customer::getCustType, customerQuery.getCustType());
        }
        // 渠道：1=美团,2=扫街,3=自然到店,4=转介绍,5=其他
        if (!StringUtils.isEmpty(customerQuery.getSource())) {
            queryWrapper.eq(Customer::getSource, customerQuery.getSource());
        }
        if(AdminUtils.hasRoleAnyMatch(ShiroUtils.getAdminId(),"管理员","超级管理员")){
            //管理员看同部门下所有人数据
            queryWrapper.in(Customer::getCreateUser,AdminUtils.getAdminsByDep(ShiroUtils.getAdminInfo().getDeptId()));
        }else{
            //其他人只查自己创建的
            queryWrapper.eq(Customer::getCreateUser, ShiroUtils.getAdminId());
        }
        queryWrapper.eq(Customer::getMark, 1);
        queryWrapper.orderByDesc(Customer::getId);

        // 查询数据
        IPage<Customer> page = new Page<>(customerQuery.getPage(), customerQuery.getLimit());
        IPage<Customer> data = customerMapper.selectPage(page, queryWrapper);
        List<Customer> customerList = data.getRecords();
        List<CustomerListVo> customerListVoList = new ArrayList<>();
        if (!customerList.isEmpty()) {
            customerList.forEach(item -> {
                CustomerListVo customerListVo = new CustomerListVo();
                // 拷贝属性
                BeanUtils.copyProperties(item, customerListVo);
                // 性别描述
                if (customerListVo.getSex()!=null&&customerListVo.getSex()>0) {
                    customerListVo.setSexName(CustomerConstant.CUSTOMER_SEX_LIST.get(customerListVo.getSex()));
                }
                // 客户类型描述
                if (customerListVo.getCustType()!=null&&customerListVo.getCustType()>0) {
                    customerListVo.setCustTypeName(CustomerConstant.CUSTOMER_CUSTTYPE_LIST.get(customerListVo.getCustType()));
                }
                // 渠道描述
                if (customerListVo.getSource()!=null&&customerListVo.getSource()>0) {
                    customerListVo.setSourceName(CustomerConstant.CUSTOMER_SOURCE_LIST.get(customerListVo.getSource()));
                }
                // 创建人名称
                if (customerListVo.getCreateUser() != null && customerListVo.getCreateUser() > 0) {
                    customerListVo.setCreateUserName(AdminUtils.getName((customerListVo.getCreateUser())));
                }
                // 修改人名称
                if (customerListVo.getUpdateUser() != null && customerListVo.getUpdateUser() > 0) {
                    customerListVo.setUpdateUserName(AdminUtils.getName((customerListVo.getUpdateUser())));
                }
                //最后探店时间
                String lastTandianTime = customerMapper.getLastTandianTime(customerListVo.getId());
                customerListVo.setLastTandianTimeStr(lastTandianTime);
                //探店次数
                //Integer tandianNum = customerMapper.getTandianNum(customerListVo.getId());
                //customerListVo.setTandianNum(customerListVo.getTdNum());
                //年龄
                if(customerListVo.getBirthday()!=null){
                    Integer diffMonth = customerMapper.getAgeForMonth(customerListVo.getBirthday());
                    String age = diffMonth>0?diffMonth/12+"岁"+diffMonth%12+"个月":"未满月";
                    customerListVo.setAge(age);
                }
                //最后沟通时间内容
                Map<String, Object> lastGoutongInfo = customerMapper.getLastGoutongInfo(customerListVo.getId());
                if(null != lastGoutongInfo){
                    customerListVo.setLastGoutongTimeStr((String) lastGoutongInfo.get("gtTime"));
                    customerListVo.setLastGoutongDesc((String) lastGoutongInfo.get("gtDesc"));
                    customerListVo.setInteractTimeStr((String) lastGoutongInfo.get("interactTime"));
                    if(null!=lastGoutongInfo.get("replyFlag")){
                        customerListVo.setReplyFlag((Integer) lastGoutongInfo.get("replyFlag"));
                        customerListVo.setReplyFlagName(GoutongConstant.GOUTONG_REPLY_LIST.get((Integer) lastGoutongInfo.get("replyFlag")));
                    }
                    if(null!=lastGoutongInfo.get("interactDesc")){
                        customerListVo.setInteractDesc((String) lastGoutongInfo.get("interactDesc"));
                    }
                }else{
                    customerListVo.setReplyFlagName("");
                }
                customerListVoList.add(customerListVo);
            });
        }
        return JsonResult.success("操作成功", customerListVoList, data.getTotal());
    }

    /**
     * 获取记录详情
     *
     * @param id 记录ID
     * @return
     */
    @Override
    public Object getInfo(Serializable id) {
        Customer entity = (Customer) super.getInfo(id);
        return entity;
    }

    /**
     * 添加或编辑记录
     *
     * @param entity 实体对象
     * @return
     */
    @Override
    public JsonResult edit(Customer entity) {
        if(entity.getCustType() == 7 && entity.getCompleteTime() == null){
            entity.setCompleteTime(new Date());
        }
        return super.edit(entity);
    }

    /**
     * 删除记录
     *
     * @param id 记录ID
     * @return
     */
    @Override
    public JsonResult deleteById(Integer id) {
        if (id == null || id == 0) {
            return JsonResult.error("记录ID不能为空");
        }
        Customer entity = this.getById(id);
        if (entity == null) {
            return JsonResult.error("记录不存在");
        }
        return super.delete(entity);
    }

    /**
     * 批量导入
     * @param request
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public JsonResult uploadExcel(HttpServletRequest request) {
        //上传数据到服务器
        JsonResult result = uploadService.uploadFile(request, "sys_customer");
        Map<String, Object> data = (Map<String, Object>) result.getData();
        String path = uploadFileConfig.getUploadFolder() + data.get("fileUrl").toString().substring(1);
        List<CustomerListVo> resultList = new ArrayList<>();
        try {
            InputStream fileStream = new FileInputStream(path);
            resultList = ExcelSimpleUtil.importExcel(fileStream,CustomerListVo.class);
        } catch (Exception e) {
            logger.error("excel解析出错！",e);
        }
        //上传不成功的放入这里 前台展示
        List<CustomerListVo> errorList =  new ArrayList<>();
        if (!resultList.isEmpty()) { //不为空
            int size = resultList.size();
            int maxPerLimit = 1000;//单次插入数据上限
            int j = (int) Math.ceil((double) size / maxPerLimit);
            int success = 0;
            for (int i = 0; i < j; i++) {
                List<CustomerListVo> insertList;
                if ((i + 1) * maxPerLimit < size) {
                    insertList = resultList.subList(i * maxPerLimit, (i + 1) * maxPerLimit);
                }else {
                    insertList = resultList.subList(i * maxPerLimit, size);
                }
                for(CustomerListVo customerListVo:insertList){
                    Customer customer = new Customer();
                    if(null!=customerListVo.getBirthday()){
                        customerListVo.setBirthdayStr(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD,customerListVo.getBirthday()));
                    }

                    if(StringUtils.isEmpty(customerListVo.getCustName())){
                        customerListVo.setErrorMsg("请输入客户姓名!");
                        errorList.add(customerListVo);
                        continue;
                    }
                    if(StringUtils.isEmpty(customerListVo.getNickName())){
                        customerListVo.setErrorMsg("请输入客户昵称!");
                        errorList.add(customerListVo);
                        continue;
                    }
                    if(StringUtils.isEmpty(customerListVo.getSexName())
                            ||StringUtils.isEmpty(CustomerConstant.CUSTOMER_SEX_LIST.get(Integer.parseInt(customerListVo.getSexName())))){
                        customerListVo.setErrorMsg("请输入正确的客户性别!");
                        errorList.add(customerListVo);
                        continue;
                    }
                    LambdaQueryWrapper<Customer> queryWrapper = new LambdaQueryWrapper<>();
                    queryWrapper.eq(Customer::getCustName,customerListVo.getCustName());
                    queryWrapper.eq(Customer::getNickName,customerListVo.getNickName());
                    queryWrapper.eq(Customer::getSex,Integer.parseInt(customerListVo.getSexName()));
                    if(null != customerListVo.getBirthday()){
                        queryWrapper.apply(" date_format(birthday,'%Y/%m/%d') = '" + DateUtils.parseDateToStr("yyyy/MM/dd",customerListVo.getBirthday())+"'");
                    }
                    Integer custCount = customerMapper.selectCount(queryWrapper);
                    if(custCount>0){
                        customerListVo.setErrorMsg("存在相同姓名、昵称、性别、出生日期!");
                        errorList.add(customerListVo);
                        continue;
                    }
                    if(StringUtils.isEmpty(customerListVo.getSourceName())
                            ||StringUtils.isEmpty(CustomerConstant.CUSTOMER_SOURCE_LIST.get(Integer.parseInt(customerListVo.getSourceName())))){
                        customerListVo.setErrorMsg("请填写正确的渠道!");
                        errorList.add(customerListVo);
                        continue;
                    }
                    if(StringUtils.isEmpty(customerListVo.getCustTypeName())
                            ||StringUtils.isEmpty(CustomerConstant.CUSTOMER_CUSTTYPE_LIST.get(Integer.parseInt(customerListVo.getCustTypeName())))){
                        customerListVo.setErrorMsg("请填写正确的客户类型!");
                        errorList.add(customerListVo);
                        continue;
                    }
                    if(StringUtils.isEmpty(customerListVo.getReplyFlagName())
                            ||StringUtils.isEmpty(GoutongConstant.GOUTONG_REPLY_LIST.get(Integer.parseInt(customerListVo.getReplyFlagName())))){
                        customerListVo.setErrorMsg("请填写正确的回复标志!");
                        errorList.add(customerListVo);
                        continue;
                    }
                    BeanUtils.copyProperties(customerListVo, customer);
                    customer.setSex(Integer.valueOf(customerListVo.getSexName()));
                    customer.setCustType(Integer.valueOf(customerListVo.getCustTypeName()));
                    customer.setSource(Integer.valueOf(customerListVo.getSourceName()));
                    customer.setCreateUser(ShiroUtils.getAdminId());
                    customer.setCreateTime(new Date());
                    customer.setTdNum(customerListVo.getTdNum());
                    if(customer.getCustType() == 7 && customer.getCompleteTime() == null){
                        //如果是成交，且没有填成交日期，就默认当日成交
                        customer.setCompleteTime(new Date());
                    }
                    super.add(customer);
                    success++;
                    if(null!=customerListVo.getLastTandianTime()){
                        //探店记录
                        Tandian tandian = new Tandian();
                        tandian.setCustId(customer.getId());
                        tandian.setTdTime(customerListVo.getLastTandianTime());
                        tandian.setCreateUser(ShiroUtils.getAdminId());
                        tandian.setCreateTime(new Date());
                        tandianMapper.insert(tandian);
                    }
                    if(null!=customerListVo.getLastGoutongTime()
                            ||null!=customerListVo.getLastGoutongDesc()){
                        //沟通记录
                        Goutong goutong = new Goutong();
                        goutong.setCustId(customer.getId());
                        goutong.setGtTime(null==customerListVo.getLastGoutongTime()?new Date():customerListVo.getLastGoutongTime());
                        goutong.setInteractTime(customerListVo.getInteractTime());
                        goutong.setInteractDesc(customerListVo.getInteractDesc());
                        goutong.setGtDesc(StringUtils.isEmpty(customerListVo.getLastGoutongDesc())?"":customerListVo.getLastGoutongDesc());
                        goutong.setReplyFlag(Integer.valueOf(customerListVo.getReplyFlagName()));
                        goutong.setCreateUser(ShiroUtils.getAdminId());
                        goutong.setCreateTime(new Date());
                        goutongMapper.insert(goutong);
                    }
                }
            }
            logger.info("成功往表 SYS_CUSTOMER 插入 {} 条数据", success);
            return JsonResult.success("操作成功",errorList);
        }
        return JsonResult.error();
    }

    /**
     * 下载模板
     * @param excelBaseParam
     */
    @Override
    public void downloadExcel(ExcelBaseParam excelBaseParam) {
        List<CustomerListVo> example = new ArrayList<>();
        CustomerListVo cust = new CustomerListVo();
        cust.setCustName("宝宝");
        cust.setNickName("baobao");
        cust.setBirthday(new Date());
        cust.setSexName("输入性别代码:1=男,2=女,3=未知");
        cust.setPhone("如：13888888888");
        cust.setAddress("");
        cust.setSourceName("输入渠道:1=美团,2=扫街,3=自然到店,4=转介绍,5=其他,6=新生堂,7=影耀");
        cust.setCustTypeName("输入客户类型:1=A,2=B,3=C,4=D,5=E,6=S,7=成交,8=F");
        cust.setReplyFlagName("输入回复标志:1=未回复,2=已回复");
        cust.setCompleteTime(new Date());
        cust.setLastTandianTime(new Date());
        cust.setLastGoutongTime(new Date());
        cust.setLastGoutongDesc("");
        cust.setInteractTime(new Date());
        cust.setInteractDesc("");
        cust.setInviteTime(new Date());
        example.add(cust);
        try{
            Workbook workbook = ExcelSimpleUtil.exportExcel(excelBaseParam.getTitleName(), excelBaseParam.getSheetName(), example, CustomerListVo.class,true);
            ExcelDownloadUtil.downloadExcel(workbook,excelBaseParam.getFileName()+".xlsx",excelBaseParam.getResponse());
        }catch (IOException e){
            logger.error("excel导出出错!",e);
        }

    }

    /**
     * 每5分钟扫描一次今天待沟通客户
     */
    @Scheduled(cron = "0 0/5 * * * ?")
    @SuppressWarnings(value = "unchecked")
    @Override
    public void pushCustGontongNotice() {
        logger.info("沟通计划消息推送定时任务执行开始......");
        LambdaQueryWrapper<Customer> queryWrapper = new LambdaQueryWrapper<>();
        //默认推送当天的
        queryWrapper.apply(" id in (select cust_id from sys_goutong where DATE_FORMAT(interact_time,'%Y-%m-%d')='"+DateUtils.getCurDateFormat(DateUtils.YYYY_MM_DD)+"')");
        queryWrapper.orderByAsc(Customer::getCreateTime);
        List<Customer> list = customerMapper.selectList(queryWrapper);
        logger.info("查询到["+list.size()+"]条待推送沟通计划");
        list.stream().forEach(customer -> {
            customer.setInteractTimeStr(DateUtils.getCurDateFormat(DateUtils.YYYY_MM_DD));
        });
        try {
            WebSocketMessage<Customer> webSocketMessage = new WebSocketMessage();
            webSocketMessage.setList(list);
            webSocketMessage.setUserFilter(username -> filterByUsername(list, username));

            webSocketMessage.setIfSendMessage(this::ifSendMessage);
            WebSocketServer.sendHandler(webSocketMessage);//推送
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        logger.info("沟通计划推送定时任务执行结束");
    }

    /**
     * 按客户登录名称信息进行分组
     */
    private List<Customer> filterByUsername(List<Customer> list,String username){
        return list.stream().filter(row->AdminUtils.getName(row.getCreateUser()).equals(username)).collect(Collectors.toList());
    }

    /**
     * 是否进行消息推送
     */
    private boolean ifSendMessage(Integer userId){
        return AdminUtils.hasRoleAnyMatch(userId,"销售","运营","超级管理员");
    }

    @Override
    public JsonResult delCustById(Integer id) {
        boolean result = this.removeById(id);
        if(!result){
            return JsonResult.error();
        }
        LambdaQueryWrapper<Tandian> tandianWrapper = new LambdaQueryWrapper<>();
        tandianWrapper.eq(Tandian::getCustId,id);
        tandianMapper.delete(tandianWrapper);

        LambdaQueryWrapper<Goutong> goutongWrapper = new LambdaQueryWrapper<>();
        goutongWrapper.eq(Goutong::getCustId,id);
        goutongMapper.delete(goutongWrapper);
        return JsonResult.success("删除成功");
    }
}