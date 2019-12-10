package com.qf.controller.api.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSONObject;
import com.qf.common.JwtConfig;
import com.qf.common.apiLimit.ApiLimitConfig;
import com.qf.common.systemLog.SystemHistoryAnnotation;
import com.qf.model.admin.HixentAppUser;
import com.qf.model.admin.HixentArea;
import com.qf.model.admin.HixentCompany;
import com.qf.model.admin.HixentDictionary;
import com.qf.model.admin.HixentPermissions;
import com.qf.model.admin.HixentProvince;
import com.qf.model.admin.HixentRole;
import com.qf.model.admin.HixentSite;
import com.qf.model.admin.HixentUser;
import com.qf.model.admin.valid.ValidHixentAppUserMore;
import com.qf.model.admin.valid.ValidHixentArea;
import com.qf.model.admin.valid.ValidHixentCompanySave;
import com.qf.model.admin.valid.ValidHixentPermissions;
import com.qf.model.admin.valid.ValidHixentRole;
import com.qf.model.admin.valid.ValidHixentUserMore;
import com.qf.model.admin.valid.ValidSaveDictionary;
import com.qf.model.fire.HixentArcProjectType;
import com.qf.model.fire.valid.ValidHixentArcProjectType;
import com.qf.service.admin.CommonService;
import com.qf.service.admin.HixentCompanyService;
import com.qf.service.admin.HixentDictionaryService;
import com.qf.service.admin.HixentMessageService;
import com.qf.service.admin.HixentPermissionsRoleService;
import com.qf.service.admin.HixentUserService;
import com.qf.service.app.HixentAppUserService;
import com.qf.service.fire.HixentArcProjectTypeService;
import com.qf.service.fire.HixentArcWarningListService;
import com.qf.util.JwtUtil;
import com.qf.util.PasswordUtil;
import com.qf.util.RedisUtil;
import com.qf.util.ReturnUtil;
import com.qf.util.StringUtil;

import io.jsonwebtoken.Claims;
import tk.mybatis.mapper.entity.Example;

@RestController
@RequestMapping("/api/user/save")
public class ApiUserSaveController {

	@Autowired
	private HixentDictionaryService hixentDictionaryService;

	@Autowired
	private HixentUserService hixentUserService;

	@Autowired
	private HixentAppUserService hixentAppUserService;

	@Autowired
	private HixentMessageService hixentMessageService;

	@Autowired
	private HixentCompanyService hixentCompanyService;

	@Autowired
	private HixentPermissionsRoleService hixentPermissionsRoleService;

	@Autowired
	private HixentArcProjectTypeService hixentArcProjectTypeService;

	@Autowired
	private HixentArcWarningListService hixentArcWarningListService;

	@Autowired
	private CommonService commonService;

	@Resource
	private RedisUtil redisUtil;

	@Resource
	private JwtConfig jwtConfig;

	@Resource
	private JwtUtil jwtUtil;

	/**
	 * 新建管理员页面获取省份和角色初始数据
	 * author RuanYu
	 */
	//@ApiLimitConfig(count = 1, time = 1000)
	@RequestMapping(value = "/getPrivinceAndRole", method = RequestMethod.POST)
	@SystemHistoryAnnotation(opration = "新建管理员页面获取省份和角色初始数据")
	public ModelMap getPrivinceOrCity() {
		try {
			
			ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes();
			HttpServletRequest request = requestAttributes.getRequest();
			String auth = request.getHeader(jwtConfig.getJwtHeader());
			auth = auth.substring(7, auth.length());
			Claims claims = jwtUtil.parseJWT(auth, jwtConfig.getSecret());
			String userId = claims.getId();
			String[] userArr = userId.split("_");
			if (!userArr[0].equals("admin")) {
				return ReturnUtil.Error("已退出，请重新登录！");
			}
			HixentUser userinfo = hixentUserService.findByUserId(userArr[1]);
			if (userinfo == null) {
				return ReturnUtil.Error("已退出，请重新登录！");
			}
			Integer roleId = userinfo.getFireRole();
			Integer provinceId=0;
			//查询角色类型
			Integer roleType = hixentUserService.getRoleType(roleId);
			if (roleType == 0) {
				// 超级管理员
				provinceId=0;
			
			} else if (roleType == 1) {
				// 普通管理员
				provinceId = userinfo.getProvinceId();			
			}
			// 获取省份
			List<HixentProvince> prinvince = hixentUserService.getPrinvince(provinceId);
			// 获取角色
			List<HixentRole> role = hixentUserService.getRole(roleType);
			JSONObject roleAndprovinceList = new JSONObject();
			roleAndprovinceList.put("prinvince", prinvince);
			roleAndprovinceList.put("role", role);
			return ReturnUtil.Success("获取省份和角色成功！", roleAndprovinceList);

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * 新增管理员省联动获取地区和项目
	 *  author RuanYu
	 */
	//@ApiLimitConfig(count = 1, time = 1000)
	@RequestMapping(value = "/getCityAndSite", method = RequestMethod.POST)
	@SystemHistoryAnnotation(opration = "联动获取地区和项目")
	public ModelMap getCity(@Valid ValidHixentArea area) {
		try {
			
			ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes();
			HttpServletRequest request = requestAttributes.getRequest();
			String auth = request.getHeader(jwtConfig.getJwtHeader());
			auth = auth.substring(7, auth.length());
			Claims claims = jwtUtil.parseJWT(auth, jwtConfig.getSecret());
			String userId = claims.getId();
			String[] userArr = userId.split("_");
			if (!userArr[0].equals("admin")) {
				return ReturnUtil.Error("已退出，请重新登录！");
			}
			HixentUser userinfo = hixentUserService.findByUserId(userArr[1]);
			if (userinfo == null) {
				return ReturnUtil.Error("已退出，请重新登录！");
			}
			Integer privinceId = area.getPrivinceId();
			Integer areaId = area.getAreaId();
			if(privinceId==0) {
				return ReturnUtil.Error("请选择省份！");
			}
			//获取地区集合
			List<HixentArea> city = hixentUserService.getCity(privinceId);
			//获取项目集合
			String bid = userinfo.getBid();
			String[] siteCordArr = bid.split(",");
		
			List<HixentSite> getsite = hixentUserService.getsite(privinceId, areaId,siteCordArr);
			
			JSONObject cityAndsiteList = new JSONObject();
			cityAndsiteList.put("city", city);
			cityAndsiteList.put("site", getsite);
			return ReturnUtil.Success("获取市区和项目成功！", cityAndsiteList);
			

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	/**
	 * 新增管理员地区联动项目
	 *  author RuanYu
	 */
	//@ApiLimitConfig(count = 1, time = 1000)
	@RequestMapping(value = "/getSiteByCity", method = RequestMethod.POST)
	@SystemHistoryAnnotation(opration = "联动获取地区和项目")
	public ModelMap getSiteByCity(@Valid ValidHixentArea area,BindingResult bindingResult) {
		try {
			if ( bindingResult.hasErrors() ) {
	            return ReturnUtil.Error("参数错误！");
	        }else{
			ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes();
			HttpServletRequest request = requestAttributes.getRequest();
			String auth = request.getHeader(jwtConfig.getJwtHeader());
			auth = auth.substring(7, auth.length());
			Claims claims = jwtUtil.parseJWT(auth, jwtConfig.getSecret());
			String userId = claims.getId();
			String[] userArr = userId.split("_");
			if (!userArr[0].equals("admin")) {
				return ReturnUtil.Error("已退出，请重新登录！");
			}
			HixentUser userinfo = hixentUserService.findByUserId(userArr[1]);
			if (userinfo == null) {
				return ReturnUtil.Error("已退出，请重新登录！");
			}
			Integer privinceId = area.getPrivinceId();
			Integer areaId = area.getAreaId();
			
			if(privinceId==null || privinceId==0) {
				return ReturnUtil.Error("请选择省份！");
			}
			
			//获取项目集合
			String bid = userinfo.getBid();
			String[] siteCordArr = bid.split(",");
			
			List<HixentSite> getsite = hixentUserService.getsite(privinceId, areaId,siteCordArr);
			
			JSONObject siteList = new JSONObject();
			
			siteList.put("site", getsite);
			return ReturnUtil.Success("获取市区和项目成功！", siteList);
			
	        }
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	/**
	 * 添加/编辑管理员 author wjr
	 */
	// @RequiresPermissions(value = {"parc_savaUser"})
	// @RequiresRoles(value = {"rarc_1"})
	//@ApiLimitConfig(count = 1, time = 1000)
	@RequestMapping(value = "/saveToUser", method = RequestMethod.POST)
	@SystemHistoryAnnotation(opration = "添加/编辑管理员")
	public ModelMap saveToUser(@Valid ValidHixentUserMore user, BindingResult bindingResult) {
		try {
			
			if (bindingResult.hasErrors()) {
				String message = "";
				List<FieldError> list = bindingResult.getFieldErrors();
				for (int i = 0; i < list.size(); i++) {
					message += list.get(i).getDefaultMessage() + ";";
				}
				return ReturnUtil.Error(message);
			}
			ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    	    HttpServletRequest request = requestAttributes.getRequest();
    		String auth      = request.getHeader(jwtConfig.getJwtHeader());
        	auth             = auth.substring(7, auth.length());
        	Claims claims    = jwtUtil.parseJWT(auth, jwtConfig.getSecret());
            String userId    = claims.getId();
            String[] userArr = userId.split("_");
            if( !userArr[0].equals("admin") ){
            	return ReturnUtil.Error("已退出，请重新登录！");
            }
        	HixentUser userinfo = hixentUserService.findByUserId(userArr[1]);
            if(userinfo == null){
            	return ReturnUtil.Error("已退出，请重新登录！");
            }
            Integer pid = userinfo.getId();
			// 获取数据
			Integer id = user.getId();
			Integer fireRole = user.getFireRole();
			Integer cid = user.getCid();
			String mobile = user.getMobile().trim();
			String account = user.getAccount().trim();
			String bid = user.getBid();
			Integer areaId = user.getAreaId();
			Integer provinceId = user.getProvinceId();
			String remark = user.getRemark();
			if (account.equals("")) {
				return ReturnUtil.Error("用户名需填写！");
			} else if (mobile.equals("")) {
				return ReturnUtil.Error("手机号需填写！");
			} else if(fireRole==0) {
				return ReturnUtil.Error("角色需选择！");
			} 
			
			String message = "";
			// 操作
			//获取角色的类型
			Integer roleType = hixentUserService.getRoleType(fireRole);
			if(roleType==0) {
				//超级管理员角色
				provinceId=0;
				areaId = 0 ;
				bid = "0" ;
			}else {
				//区域管理员
				//管控管理员
				if(provinceId==null || provinceId==0) {
					return ReturnUtil.Error("省份需选择！");
				}
				if("0".equals(bid)) {
					return ReturnUtil.Error("请选择项目！");
				}
			}
			if(roleType==2){
				//管控管理员
				if(areaId==null || areaId==0) {
					return ReturnUtil.Error("地区需选择！");
				}
				
			}
			if (id == 0) {
				// 获取数据
				String salt = StringUtil.getRandomString(32);
				String password = user.getPassword().trim();
				String password2 = user.getPassword2().trim();
				String passwordn = PasswordUtil.createCustomPwd(password, salt);
				String uid = PasswordUtil.createCustomPwd(account, salt);
				if (user.getPassword().trim().equals("")) {
					return ReturnUtil.Error("密码需填写！");
				} else if (!password.equals(password2)) {
					return ReturnUtil.Error("两次密码输入的不一致！");
				} else {
					// 唯一性判断
					HixentUser userinfo1 = hixentUserService.getUserinfoByMobile(mobile);
					HixentUser userinfo2 = hixentUserService.findByUsername(account);
					HixentUser userinfo3 = hixentUserService.findByUserId(uid);
					if (userinfo1 != null || userinfo2 != null || userinfo3 != null) {
						return ReturnUtil.Error("用户名称或手机号重复,请重试！");
					}
					hixentUserService.insertUser(uid, account, passwordn, mobile, salt, cid, fireRole, bid, provinceId, areaId, remark, pid);
					// 添加
					message = "添加成功！";
				}
			} else {
				// 获取数据
				String salt = StringUtil.getRandomString(32);
				String uid = PasswordUtil.createCustomPwd(account, salt);
				// 获取修改前的数据
				HixentUser userinfos = hixentUserService.getAdminById(id);
				
				if (fireRole == 0) {
					fireRole = userinfos.getFireRole();
				} else {
					
					// 编辑
					// 唯一性判断
					HixentUser userinfo1 = hixentUserService.getUserinfoByMobile(mobile);
					HixentUser userinfo2 = hixentUserService.findByUsername(account);
					HixentUser userinfo3 = hixentUserService.findByUserId(uid);
					if(userinfo1 != null && userinfo1.getId()!=id) {
						return ReturnUtil.Error("用户名称或手机号重复,请重试！");
					}
					if ( userinfo2 != null  && userinfo2.getId()!=id) {
						
						return ReturnUtil.Error("用户名称或手机号重复,请重试！");
					}
                    if ( userinfo3 != null  && userinfo3.getId()!=id) {
						
						return ReturnUtil.Error("用户名称或手机号重复,请重试！");
					}
					
					hixentUserService.updateUser(id, account, mobile,fireRole, bid,provinceId,areaId,remark);
					message = "修改成功！";
				}
			}
			return ReturnUtil.Success(message);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * 添加/编辑角色 author Vareck
	 */
	// @RequiresPermissions(value = {"parc_saveRole"})
	//@ApiLimitConfig(count = 1, time = 1000)
	@SystemHistoryAnnotation(opration = "添加/编辑角色")
	@RequestMapping(value = "/saveUser", method = RequestMethod.POST)
	public ModelMap saveUser(@Valid ValidHixentRole role) {
		try {
			// 获取数据
			Integer role_id = role.getRoleId();
			String role_name = role.getName();
			String menu_id_list = role.getMenuIdList();
			String role_desc = role.getRoleDesc();
			// 对应系统的角色判断
			String message = "";
			// 操作
			if (role_id == 0) {
				// 唯一性判断
				List<HixentRole> rlist = hixentPermissionsRoleService.getRoleAllList2(role_name);
				if (rlist.size() > 0) {
					return ReturnUtil.Error("角色名称重复");
				}
				// 添加
				hixentPermissionsRoleService.insertRole(role_name, menu_id_list, role_desc);
				message = "添加成功！";
			} else {
				// 编辑
				hixentPermissionsRoleService.updateRole(role_id, role_name, menu_id_list, role_desc);
				message = "修改成功！";
			}
			return ReturnUtil.Success(message);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * 添加/编辑权限 author Vareck
	 */
	// @RequiresPermissions(value = {"parc_savePermissions"})
	//@ApiLimitConfig(count = 1, time = 1000)
	@SystemHistoryAnnotation(opration = "添加/编辑权限")
	@RequestMapping(value = "/savePermissions", method = RequestMethod.POST)
	public ModelMap savePermissions(@Valid ValidHixentPermissions permissions) {
		try {
			// 获取数据
			Integer menuId = permissions.getMenuId();
			String menuIcon = permissions.getMenuIcon();
			String menuUrl = permissions.getMenuUrl();
			Integer parentId = permissions.getParentId();
			String actionName = permissions.getActionName();
			String menuName = permissions.getMenuName();
			Integer setOrder = permissions.getSetOrder();
			if (actionName.trim().equals("") || menuName.trim().equals("")) {
				return ReturnUtil.Error("请填写必要的参数！");
			}
			// 获取用户信息
			// HixentUser userinfo = (HixentUser) SecurityUtils.getSubject().getPrincipal();
			ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes();
			HttpServletRequest request = requestAttributes.getRequest();
			String auth = request.getHeader(jwtConfig.getJwtHeader());
			auth = auth.substring(7, auth.length());
			Claims claims = jwtUtil.parseJWT(auth, jwtConfig.getSecret());
			String userId = claims.getId();
			String[] userArr = userId.split("_");
			if (!userArr[0].equals("admin")) {
				return ReturnUtil.Error("已退出，请重新登录！");
			}
			HixentUser userinfo = hixentUserService.findByUserId(userArr[1]);
			if (userinfo == null) {
				return ReturnUtil.Error("已退出，请重新登录！");
			}
			Map<String, Object> jsonMap = new HashMap<>();
			String message = "";
			// 操作
			if (menuId == 0) {
				// 唯一性判断
				List<HixentPermissions> plist1 = hixentPermissionsRoleService.getPermissionsAllList2(menuName);
				List<HixentPermissions> plist2 = hixentPermissionsRoleService.getPermissionsAllList2(actionName);
				if (plist1.size() > 0 || plist2.size() > 0) {
					return ReturnUtil.Error("权限名称重复");
				}
				// 添加
				hixentPermissionsRoleService.insertPermissions(menuIcon, menuUrl, parentId, actionName, menuName,
						setOrder);
				message = "添加成功！";
			} else {
				// 编辑
				hixentPermissionsRoleService.updatePermissions(menuId, menuIcon, menuUrl, parentId, actionName,
						menuName, setOrder);
				message = "修改成功！";
			}
			// 重新生成权限树
			if (userinfo.getFireRole() > 0) {
				jsonMap.put("fire_menu", commonService.getPermissions(userinfo.getUid()));
			} else {
				jsonMap.put("fire_menu", "");
			}
			JSONObject json = new JSONObject(jsonMap);
			return ReturnUtil.Success(message, json);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * 添加/编辑角色 author Vareck
	 */
	// @RequiresPermissions(value = {"parc_saveRole"})
	@ApiLimitConfig(count = 1, time = 1000)
	@SystemHistoryAnnotation(opration = "添加/编辑角色")
	@RequestMapping(value = "/saveRole", method = RequestMethod.POST)
	public ModelMap saveRole(@Valid ValidHixentRole role) {
		try {
			// 获取数据
			Integer role_id = role.getRoleId();
			String role_name = role.getName();
			String menu_id_list = role.getMenuIdList();
			String role_desc = role.getRoleDesc();
			String message = "";
			// 操作
			if (role_id == 0) {
				// 唯一性判断
				List<HixentRole> rlist = hixentPermissionsRoleService.getRoleAllList2(role_name);
				if (rlist.size() > 0) {
					return ReturnUtil.Error("角色名称重复");
				}
				// 添加
				hixentPermissionsRoleService.insertRole(role_name, menu_id_list, role_desc);
				message = "添加成功！";
			} else {
				// 编辑
				hixentPermissionsRoleService.updateRole(role_id, role_name, menu_id_list, role_desc);
				message = "修改成功！";
			}
			return ReturnUtil.Success(message);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * 添加/编辑公司数据 author Vareck
	 */
	// @RequiresPermissions(value = {"parc_saveComPany"})
	//@ApiLimitConfig(count = 1, time = 1000)
	@SystemHistoryAnnotation(opration = "添加/编辑公司数据")
	@RequestMapping(value = "/saveCompany", method = RequestMethod.POST)
	public ModelMap saveCompany(@Valid ValidHixentCompanySave company) {
		try {
			if (company.getName().trim().equals("")) {
				return ReturnUtil.Error("公司名称必须填写！");
			} else {
				// 整理数据
				HixentCompany data = new HixentCompany();
				data.setName(company.getName().trim());
				data.setLogoUrl(company.getLogoUrl().trim());
				String message = "";
				// 操作
				if (company.getId() == 0) {
					// 判断唯一性
					HixentCompany info = new HixentCompany();
					info.setName(company.getName().trim());
					HixentCompany cInfo = hixentCompanyService.selectOne(info);
					if (cInfo == null) {
						// 添加
						int id = hixentCompanyService.insert(data);
						redisUtil.set("company_name_" + String.valueOf(id), company.getName().trim());
						redisUtil.set("company_logo_" + String.valueOf(id), company.getLogoUrl().trim());
						message = "添加成功！";
					} else {
						return ReturnUtil.Error("公司名重复！");
					}
				} else {
					// 编辑
					Example example = new Example(HixentCompany.class);
					example.createCriteria().andCondition("cid = ", company.getId());
					hixentCompanyService.update(data, example);
					redisUtil.set("company_name_" + company.getId(), company.getName().trim());
					redisUtil.set("company_logo_" + company.getId(), company.getLogoUrl().trim());
					message = "修改成功！";
				}
				return ReturnUtil.Success(message);
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * 添加/编辑数据字典 author Vareck
	 */
	// @RequiresRoles(value = {"rarc_1"})
	//@ApiLimitConfig(count = 1, time = 1000)
	@SystemHistoryAnnotation(opration = "添加/编辑数据字典")
	@RequestMapping(value = "/saveDictionary", method = RequestMethod.POST)
	public ModelMap saveDictionary(@Valid ValidSaveDictionary dictionary, BindingResult bindingResult) {
		try {
			if (bindingResult.hasErrors()) {
				return ReturnUtil.Error("参数错误！");
			} else {
				// 整理数据
				HixentDictionary data = new HixentDictionary();
				data.setTypename(dictionary.getTypename());
				data.setDkey(dictionary.getDkey());
				data.setDvalue(dictionary.getDvalue());
				String message = "";
				// 操作
				if (dictionary.getDid() == 0) {
					// 添加
					hixentDictionaryService.insert(data);
					message = "添加成功！";
				} else {
					// 编辑
					Example example = new Example(HixentDictionary.class);
					example.createCriteria().andCondition("did = ", dictionary.getDid());
					hixentDictionaryService.update(data, example);
					message = "修改成功！";
				}
				redisUtil.del("fire_dictionary_info");
				return ReturnUtil.Success(message);
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * 修改密码 author Vareck
	 */
	//@ApiLimitConfig(count = 1, time = 1000)
	@RequestMapping(value = "/changePassword", method = RequestMethod.POST)
	@SystemHistoryAnnotation(opration = "登录情况下修改密码")
	public ModelMap changePassword(@Valid ValidHixentUserMore user) {
		try {
			// 获取数据
			String password = user.getPassword().trim();
			String password2 = user.getPassword2().trim();
			// 数据验证
			if (password.equals("")) {
				return ReturnUtil.Error("密码需填写！");
			} else if (!password.equals(password2)) {
				return ReturnUtil.Error("两次密码输入的不一致！");
			} else {
				// 获取用户信息
				// HixentUser userinfo = (HixentUser) SecurityUtils.getSubject().getPrincipal();
				ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
						.getRequestAttributes();
				HttpServletRequest request = requestAttributes.getRequest();
				String auth = request.getHeader(jwtConfig.getJwtHeader());
				auth = auth.substring(7, auth.length());
				Claims claims = jwtUtil.parseJWT(auth, jwtConfig.getSecret());
				String userId = claims.getId();
				String[] userArr = userId.split("_");
				if (!userArr[0].equals("admin")) {
					return ReturnUtil.Error("已退出，请重新登录！");
				}
				HixentUser userinfo = hixentUserService.findByUserId(userArr[1]);
				if (userinfo == null) {
					return ReturnUtil.Error("已退出，请重新登录！");
				}
				String salt = userinfo.getSalt();
				String passwordStart = userinfo.getPassword();
				String passwordEnd = PasswordUtil.createCustomPwd(password, salt);
				if (passwordStart.trim().equals(passwordEnd)) {
					return ReturnUtil.Error("与旧密码一致，密码修改失败！");
				} else {
					hixentUserService.updateUserPassWord(userinfo.getId(), passwordEnd);
					return ReturnUtil.Success("密码修改成功！");
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * 管理员添加App用户 author wjr
	 */
	// @RequiresPermissions(value = {"parc_appUserSave"})
	//@ApiLimitConfig(count = 1, time = 1000)
	@RequestMapping(value = "/addToAppUser", method = RequestMethod.POST)
	@SystemHistoryAnnotation(opration = "管理员添加App用户")
	public ModelMap addToAppUser(@Valid ValidHixentAppUserMore user, BindingResult bindingResult) {
		try {
			if (bindingResult.hasErrors()) {
				String message = "";
				List<FieldError> list = bindingResult.getFieldErrors();
				for (int i = 0; i < list.size(); i++) {
					message += list.get(i).getDefaultMessage() + ";";
				}
				return ReturnUtil.Error(message);
			}
			ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes();
			HttpServletRequest request = requestAttributes.getRequest();
			String auth = request.getHeader(jwtConfig.getJwtHeader());
			auth = auth.substring(7, auth.length());
			Claims claims = jwtUtil.parseJWT(auth, jwtConfig.getSecret());
			String userId = claims.getId();
			String[] userArr = userId.split("_");
			if (!userArr[0].equals("admin")) {
				return ReturnUtil.Error("已退出，请重新登录！");
			}
			HixentUser userinfo = hixentUserService.findByUserId(userArr[1]);
			if (userinfo == null) {
				return ReturnUtil.Error("已退出，请重新登录！");
			}
			// 获取数据
			Integer pid = userinfo.getId();
			Integer state = user.getState();
			  state=1;
			String mobile = user.getMobile().trim();
			String email = user.getEmail().trim();
			String account = user.getAccount().trim();
			String salt = StringUtil.getRandomString(32);
			String password = user.getPassword().trim();
			String passwordn = PasswordUtil.createCustomPwd(password, salt);
			String uid = PasswordUtil.createCustomPwd(account, salt);
			String remark = user.getRemark();
			if (account.equals("")) {
				return ReturnUtil.Error("用户名需填写！");
			} else if (mobile.equals("")) {
				return ReturnUtil.Error("手机号需填写！");
			} else if (pid <= 0) {
				return ReturnUtil.Error("请选择从属管理员的id！");
			}
			// 获取数据
		
			
			if (user.getPassword().trim().equals("")) {
				return ReturnUtil.Error("密码需填写！");
			} else {
				// 唯一性判断
				HixentAppUser userinfo1 = hixentAppUserService.getAppUserinfoByMobile(mobile);
				HixentAppUser userinfo2 = hixentAppUserService.findByAppUsername(account);
				HixentAppUser userinfo3 = hixentAppUserService.findByAppUserId(uid);
				if (userinfo1 != null || userinfo2 != null || userinfo3 != null) {
					return ReturnUtil.Error("用户名称或手机号重复,请重试！");
				}
				hixentAppUserService.addAppUser(account, salt, passwordn, mobile, email, state, pid, uid,remark);
				
				
			}
			
			return ReturnUtil.Success("添加app用户成功！");
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * 编辑App用户 author Vareck
	 */
	// @RequiresPermissions(value = {"parc_appUserSave"})
	//@ApiLimitConfig(count = 1, time = 1000)
	@RequestMapping(value = "/saveToAppUser", method = RequestMethod.POST)
	@SystemHistoryAnnotation(opration = "编辑App用户")
	public ModelMap saveToAppUser(@Valid ValidHixentAppUserMore user, BindingResult bindingResult) {
		try {
			if (bindingResult.hasErrors()) {
				String message = "";
				List<FieldError> list = bindingResult.getFieldErrors();
				for (int i = 0; i < list.size(); i++) {
					message += list.get(i).getDefaultMessage() + ";";
				}
				return ReturnUtil.Error(message);
			}
			ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes();
			HttpServletRequest request = requestAttributes.getRequest();
			String auth = request.getHeader(jwtConfig.getJwtHeader());
			auth = auth.substring(7, auth.length());
			Claims claims = jwtUtil.parseJWT(auth, jwtConfig.getSecret());
			String userId = claims.getId();
			String[] userArr = userId.split("_");
			if (!userArr[0].equals("admin")) {
				return ReturnUtil.Error("已退出，请重新登录！");
			}
			HixentUser userinfo = hixentUserService.findByUserId(userArr[1]);
			if (userinfo == null) {
				return ReturnUtil.Error("已退出，请重新登录！");
			}
			// 获取数据
			Integer pid = userinfo.getId();
			String mobile = user.getMobile().trim();
			String email = user.getEmail().trim();
			String account = user.getAccount().trim();
			String remark = user.getRemark();
			Integer id = user.getId();
			String salt = StringUtil.getRandomString(32);
			
			String uid = PasswordUtil.createCustomPwd(account, salt);
			if (account.equals("")) {
				return ReturnUtil.Error("用户名需填写！");
			} else if (mobile.equals("")) {
				return ReturnUtil.Error("手机号需填写！");
			} else if (pid <= 0) {
				return ReturnUtil.Error("请选择从属管理员的id！");
			}

			
			//如果不是超级管理员
			Integer roleType= userinfo.getRoleType();
			if(roleType!=0) {
				//查询该管控人员是否是属于该管理员
				
				HixentAppUser appAdminById = hixentAppUserService.getAppAdminById(id);
			  if(appAdminById.getPid()!=userinfo.getId()) {
				  return ReturnUtil.Error("该管控人员不属于该管理员管理！");
			  }
			}
			
				// 唯一性判断
				HixentAppUser userinfo1 = hixentAppUserService.getAppUserinfoByMobile(mobile);
				HixentAppUser userinfo2 = hixentAppUserService.findByAppUsername(account);
				HixentAppUser userinfo3 = hixentAppUserService.findByAppUserId(uid);
				if(userinfo1 != null && userinfo1.getId()!=id) {
					return ReturnUtil.Error("用户名称或手机号重复,请重试！");
				}
				if ( userinfo2 != null  && userinfo2.getId()!=id) {
					
					return ReturnUtil.Error("用户名称或手机号重复,请重试！");
				}
                if ( userinfo3 != null  && userinfo3.getId()!=id) {
					
					return ReturnUtil.Error("用户名称或手机号重复,请重试！");
				}
				// 编辑
				hixentAppUserService.updateAppUser(id, account, mobile, email, remark);
			
				
			
				
				return ReturnUtil.Success("修改成功！");

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * 添加/编辑虚拟分组 author wjr
	 */
	// @RequiresPermissions(value = {"parc_savePermissions"})
	//@ApiLimitConfig(count = 1, time = 1000)
	@SystemHistoryAnnotation(opration = "添加/编辑分组")
	@RequestMapping(value = "/saveProject", method = RequestMethod.POST)
	public ModelMap saveProject(@Valid ValidHixentArcProjectType project) {
		try {
			// 获取数据
			Integer type = project.getType();
			String name = project.getName();
			String address = project.getAddress();
			if (name.trim().equals("") || address.trim().equals("")) {
				return ReturnUtil.Error("请填写必要的参数！");
			}
			// 获取用户信息
			// HixentUser userinfo = (HixentUser) SecurityUtils.getSubject().getPrincipal();
			ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes();
			HttpServletRequest request = requestAttributes.getRequest();
			String auth = request.getHeader(jwtConfig.getJwtHeader());
			auth = auth.substring(7, auth.length());
			Claims claims = jwtUtil.parseJWT(auth, jwtConfig.getSecret());
			String userId = claims.getId();
			String[] userArr = userId.split("_");
			if (!userArr[0].equals("admin")) {
				return ReturnUtil.Error("已退出，请重新登录！");
			}
			HixentUser userinfo = hixentUserService.findByUserId(userArr[1]);
			if (userinfo == null) {
				return ReturnUtil.Error("已退出，请重新登录！");
			}
			Map<String, Object> jsonMap = new HashMap<>();
			String message = "";
			Integer adminId = userinfo.getId();
			String phone = userinfo.getMobile();
			// 操作
			if (type == 0) {
				// 唯一性判断
				List<HixentArcProjectType> plist = hixentArcProjectTypeService.checkName(name);

				if (plist.size() > 0) {
					return ReturnUtil.Error("分组名称已存在");
				}
				// 添加
				hixentArcProjectTypeService.insertProject(name, address, adminId, phone);
				message = "添加成功！";
			} else {
				// 编辑
				hixentArcProjectTypeService.updateProject(name, address, phone, type);
				message = "修改成功！";
			}
			return ReturnUtil.Success(message);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * 处理报警信息 author wjr
	 */
	// @RequiresPermissions(value = {"parc_savePermissions"})
	//@ApiLimitConfig(count = 1, time = 1000)
	@SystemHistoryAnnotation(opration = "处理报警信息")
	@RequestMapping(value = "/dealWarning", method = RequestMethod.POST)
	public ModelMap dealWarning() {
		try {
			// 获取用户信息
			// HixentUser userinfo = (HixentUser) SecurityUtils.getSubject().getPrincipal();
			ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes();
			HttpServletRequest request = requestAttributes.getRequest();
			String auth = request.getHeader(jwtConfig.getJwtHeader());
			auth = auth.substring(7, auth.length());
			Claims claims = jwtUtil.parseJWT(auth, jwtConfig.getSecret());
			String userId = claims.getId();
			String[] userArr = userId.split("_");
			if (!userArr[0].equals("admin")) {
				return ReturnUtil.Error("已退出，请重新登录！");
			}
			HixentUser userinfo = hixentUserService.findByUserId(userArr[1]);
			if (userinfo == null) {
				return ReturnUtil.Error("已退出，请重新登录！");
			}
			String id = request.getParameter("id");
			
			hixentArcWarningListService.dealWarningList(id);
			String message = "处理成功！";
			return ReturnUtil.Success(message);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * 重置密码 author wjr
	 */
	// @RequiresPermissions(value = {"parc_savePermissions"})
	//@ApiLimitConfig(count = 1, time = 1000)
	@SystemHistoryAnnotation(opration = " 重置密码")
	@RequestMapping(value = "/resetPassWord", method = RequestMethod.POST)
	public ModelMap resetPassWord(Integer id ) {
		try {
			// 获取用户信息
			// HixentUser userinfo = (HixentUser) SecurityUtils.getSubject().getPrincipal();
			ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes();
			HttpServletRequest request = requestAttributes.getRequest();
			String auth = request.getHeader(jwtConfig.getJwtHeader());
			auth = auth.substring(7, auth.length());
			Claims claims = jwtUtil.parseJWT(auth, jwtConfig.getSecret());
			String userId = claims.getId();
			String[] userArr = userId.split("_");
			if (!userArr[0].equals("admin")) {
				return ReturnUtil.Error("已退出，请重新登录！");
			}
			HixentUser userinfo = hixentUserService.findByUserId(userArr[1]);
			if (userinfo == null) {
				return ReturnUtil.Error("已退出，请重新登录！");
			}
			HixentUser admininfo = hixentUserService.getAdminById(id);
			String salt = admininfo.getSalt();
			String password = "123456";
			String passwordn = PasswordUtil.createCustomPwd(password, salt);
			hixentUserService.updateUserPassWord(id, passwordn);
			return ReturnUtil.Success("重置成功！");
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	/**
	 * 审核管控人员 author RuanYU
	 */
	// @RequiresPermissions(value = {"parc_savePermissions"})
	//@ApiLimitConfig(count = 1, time = 1000)
	@SystemHistoryAnnotation(opration = " 审核管控人员")
	@RequestMapping(value = "/auditAppUser", method = RequestMethod.POST)
	public ModelMap auditAppUser(Integer appUserId,Integer type ) {
		try {
			// 获取用户信息
			// HixentUser userinfo = (HixentUser) SecurityUtils.getSubject().getPrincipal();
			ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes();
			HttpServletRequest request = requestAttributes.getRequest();
			String auth = request.getHeader(jwtConfig.getJwtHeader());
			auth = auth.substring(7, auth.length());
			Claims claims = jwtUtil.parseJWT(auth, jwtConfig.getSecret());
			String userId = claims.getId();
			String[] userArr = userId.split("_");
			if (!userArr[0].equals("admin")) {
				return ReturnUtil.Error("已退出，请重新登录！");
			}
			HixentUser userinfo = hixentUserService.findByUserId(userArr[1]);
			if (userinfo == null) {
				return ReturnUtil.Error("已退出，请重新登录！");
			}
			//如果不是超级管理员
			Integer roleType= userinfo.getRoleType();
			if(roleType!=0) {
				//查询该管控人员是否是属于该管理员
				
				HixentAppUser appAdminById = hixentAppUserService.getAppAdminById(appUserId);
			  if(appAdminById.getPid()!=userinfo.getId()) {
				  return ReturnUtil.Error("该管控人员不属于该管理员管理！");
			  }
			}
			
		  Integer auditAppUser = hixentAppUserService.auditAppUser(appUserId);
		  if(auditAppUser>0) {
			  return ReturnUtil.Success("审核管控人员成功！");  
		  }else {
			  return ReturnUtil.Error("审核管控人员失败！");  
		  }
			
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

}