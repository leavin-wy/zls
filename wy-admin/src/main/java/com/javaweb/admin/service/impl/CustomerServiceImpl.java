package com.javaweb.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
        if(StringUtils.isNotEmpty(customerQuery.getInteractTimeStr())){
            queryWrapper.eq(Customer::getInteractTime,DateUtils.dateTime(DateUtils.YYYY_MM_DD,customerQuery.getInteractTimeStr()));
        }
        // 姓名/昵称
        if (!StringUtils.isEmpty(customerQuery.getKeywords())) {
            queryWrapper.like(Customer::getCustName, customerQuery.getKeywords()).or().like(Customer::getNickName, customerQuery.getKeywords());
        }

        // 性别：1=男,2=女,3=未知
        if (!StringUtils.isEmpty(customerQuery.getSex())) {
            queryWrapper.eq(Customer::getSex, customerQuery.getSex());
        }
        // 客户类型： 1=A,2=B,3=C,4=D,5=E,6=S,7=成交
        if (!StringUtils.isEmpty(customerQuery.getCustType())) {
            queryWrapper.eq(Customer::getCustType, customerQuery.getCustType());
        }
        // 渠道：1=美团,2=扫街,3=自然到店,4=转介绍,5=其他
        if (!StringUtils.isEmpty(customerQuery.getSource())) {
            queryWrapper.eq(Customer::getSource, customerQuery.getSource());
        }
        if(2==ShiroUtils.getAdminInfo().getDataType()){
            //如果是私有权限，则只查自己创建的
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
                customerListVo.setLastTandianTime(lastTandianTime);
                //探店次数
                Integer tandianNum = customerMapper.getTandianNum(customerListVo.getId());
                customerListVo.setTandianNum(tandianNum);
                //年龄
                if(customerListVo.getBirthday()!=null){
                    Integer diffMonth = customerMapper.getAgeForMonth(customerListVo.getBirthday());
                    String age = diffMonth>0?diffMonth/12+"岁"+diffMonth%12+"个月":"未满月";
                    customerListVo.setAge(age);
                }
                //最后沟通时间内容
                Map<String, Object> lastGoutongInfo = customerMapper.getLastGoutongInfo(customerListVo.getId());
                if(null != lastGoutongInfo){
                    customerListVo.setLastGoutongTime((String) lastGoutongInfo.get("gtTime"));
                    customerListVo.setLastGoutongDesc((String) lastGoutongInfo.get("gtDesc"));
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
            //resultList = ExcelSimpleUtil.importBigExcel(path,CustomerListVo.class,false);
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
                    Date completeTime = DateUtils.parseDate(customerListVo.getCompleteTimeStr());

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
                    Integer custCount = customerMapper.selectCount(queryWrapper);
                    if(custCount>0){
                        customerListVo.setErrorMsg("存在相同客户姓名、昵称、性别!");
                        errorList.add(customerListVo);
                        continue;
                    }
                    if(StringUtils.isEmpty(customerListVo.getSourceName())
                            ||StringUtils.isEmpty(CustomerConstant.CUSTOMER_SOURCE_LIST.get(Integer.parseInt(customerListVo.getSourceName())))){
                        customerListVo.setErrorMsg("请输入正确的渠道!");
                        errorList.add(customerListVo);
                        continue;
                    }
                    if(StringUtils.isEmpty(customerListVo.getCustTypeName())
                            ||StringUtils.isEmpty(CustomerConstant.CUSTOMER_CUSTTYPE_LIST.get(Integer.parseInt(customerListVo.getCustTypeName())))){
                        customerListVo.setErrorMsg("请输入正确的客户类型!");
                        errorList.add(customerListVo);
                        continue;
                    }
                    if(StringUtils.isNotEmpty(customerListVo.getLastGoutongTime())){
                        Date goutong = DateUtils.parseDate(customerListVo.getLastGoutongTime());
                        if(goutong==null){
                            customerListVo.setErrorMsg("沟通时间格式不正确!");
                            errorList.add(customerListVo);
                            continue;
                        }
                    }
                    Date birthday = DateUtils.parseDate(customerListVo.getBirthdayStr());
                    if(StringUtils.isNotEmpty(customerListVo.getBirthdayStr())){
                        if(birthday==null){
                            customerListVo.setErrorMsg("出生时间格式不正确!");
                            errorList.add(customerListVo);
                            continue;
                        }
                    }
                    if(StringUtils.isNotEmpty(customerListVo.getCompleteTimeStr())){
                        Date tandianTime = DateUtils.parseDate(customerListVo.getCompleteTimeStr());
                        if(tandianTime==null){
                            customerListVo.setErrorMsg("探店时间格式不正确!");
                            errorList.add(customerListVo);
                            continue;
                        }
                    }
                    Date interactTime = DateUtils.parseDate(customerListVo.getInteractTimeStr());
                    if(StringUtils.isNotEmpty(customerListVo.getInteractTimeStr())){
                        if(interactTime==null){
                            customerListVo.setErrorMsg("下次沟通时间格式不正确!");
                            errorList.add(customerListVo);
                            continue;
                        }
                    }
                    BeanUtils.copyProperties(customerListVo, customer);
                    customer.setBirthday(birthday);
                    customer.setSex(Integer.valueOf(customerListVo.getSexName()));
                    customer.setCompleteTime(completeTime);
                    customer.setInteractTime(interactTime);
                    customer.setCreateUser(ShiroUtils.getAdminId());
                    customer.setCreateTime(new Date());
                    super.add(customer);
                    success++;
                    if(StringUtils.isNotEmpty(customerListVo.getLastTandianTime())){
                        //探店记录
                        Tandian tandian = new Tandian();
                        tandian.setCustId(customer.getId());
                        tandian.setTdTime(DateUtils.parseDate(customerListVo.getLastTandianTime()));
                        tandian.setCreateUser(ShiroUtils.getAdminId());
                        tandian.setCreateTime(new Date());
                        tandianMapper.insert(tandian);
                    }
                    if(StringUtils.isNotEmpty(customerListVo.getLastGoutongTime())){
                        Goutong goutong = new Goutong();
                        goutong.setCustId(customer.getId());
                        goutong.setGtTime(DateUtils.parseDate(customerListVo.getLastGoutongTime()));
                        goutong.setGtDesc(customerListVo.getLastGoutongDesc());
                        goutong.setCreateUser(ShiroUtils.getAdminId());
                        goutong.setCreateTime(new Date());
                        goutongMapper.insert(goutong);
                    }
                    logger.info("客户信息主键id ：{}",customer.getId());
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
        cust.setCustName("张三");
        cust.setNickName("zhangs");
        cust.setBirthdayStr("填写格式：2021-05-18");
        cust.setSexName("输入性别代码:1=男,2=女,3=未知");
        cust.setPhone("如：13888888888");
        cust.setAddress("");
        cust.setSourceName("输入渠道:1=美团,2=扫街,3=自然到店,4=转介绍,5=其他");
        cust.setCustTypeName("输入客户类型:1=A,2=B,3=C,4=D,5=E,6=S,7=成交");
        cust.setCompleteTimeStr("填写格式：2022-05-18");
        cust.setLastTandianTime("填写格式：2020-05-18");
        cust.setLastGoutongTime("填写格式：2021-05-18");
        cust.setLastGoutongDesc("");
        cust.setInteractTimeStr("填写格式：2023-05-18");
        example.add(cust);
        try{
            Workbook workbook = ExcelSimpleUtil.exportExcel(excelBaseParam.getTitleName(), excelBaseParam.getSheetName(), example, CustomerListVo.class,true);
            ExcelDownloadUtil.downloadExcel(workbook,excelBaseParam.getFileName()+".xlsx",excelBaseParam.getResponse());
        }catch (IOException e){
            logger.error("excel导出出错!",e);
        }

    }

    /**
     * cron： 每5分钟扫描一次
     */
    @Scheduled(cron = "0 0/2 * * * ?")
    @SuppressWarnings(value = "unchecked")
    @Override
    public void pushCustGontongNotice() {
        logger.info("沟通计划消息推送定时任务执行开始......");
        LambdaQueryWrapper<Customer> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ge(Customer::getInteractTime,new Date());
        queryWrapper.le(Customer::getInteractTime,DateUtils.getDateByN(3));
        queryWrapper.orderByAsc(Customer::getInteractTime);
        List<Customer> list = customerMapper.selectList(queryWrapper);
        list.stream().forEach(customer -> {
            customer.setInteractTimeStr(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD,customer.getInteractTime()));
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
        return AdminUtils.hasRoleAnyMatch(userId,"运营","管理员","超级管理员");
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