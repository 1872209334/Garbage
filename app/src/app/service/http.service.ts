/**
 * http服务
 *
 * */
import {Injectable} from '@angular/core';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import {HttpClient, HttpHeaders, HttpResponse} from "@angular/common/http";
import {AlertMsgService} from "./alert.msg.service";
import {ApiConfig} from "../app.api.config";
import {LoginPage} from "../../pages/login/login";
import {App,NavController} from "ionic-angular";

@Injectable()
export class HttpService {
  //构造方法
  constructor(
    private http: HttpClient,
    private alertMsgService: AlertMsgService,
    public app: App,
  ) {
  }

  public loginUrl = ApiConfig.arcServerUrl + "/getApp/token";
  public sendSocketUrl = ApiConfig.arcServerUrl + "/app/sendSocketMessage";
  public clearMqttVoiceUrl = ApiConfig.arcServerUrl + "/app/mqttRemoveWarning";
  public MqttDeviceInfoUrl = ApiConfig.arcServerUrl + "/app/mqttUpdateDeviceInfo";
  public logoutUrl = ApiConfig.arcServerUrl + "/app/user/common/logoutApp";
  public getProjectListUrl = ApiConfig.arcServerUrl + "/app/fire/list/projectTypeMapList";
  public getProjectDeviceUrl = ApiConfig.arcServerUrl + "/app/fire/list/deviceAppList";
  public getWarningListUrl = ApiConfig.arcServerUrl + "/app/fire/list/fireWarningAppList";
  public getdeviceWarningListUrl = ApiConfig.arcServerUrl + "/app/fire/list/deviceWarningAppList";
  public getVerifyUrl = ApiConfig.arcServerUrl + "/getApp/registerCode";
  public getRegisterUrl = ApiConfig.arcServerUrl + "/getApp/register";
  //个人修改密码验证码
  public getPasswordCodeUrl = ApiConfig.arcServerUrl + "/getApp/passwordCode";
  //个人修改密码接口
  public setPasswordUrl = ApiConfig.arcServerUrl + "/app/user/common/forgetPassword";
  //忘记密码获取验证码
  public forgetPasswordCodeUrl = ApiConfig.arcServerUrl + "/getApp/forgetPasswordCode";
  //忘记密码验证验证码
  public verifyCodeUrl = ApiConfig.arcServerUrl + "/getApp/verifyCode";
  //忘记密码接口
  public forgetPasswordChangeUrl = ApiConfig.arcServerUrl + "/getApp/forgetPasswordChange";
  public getTerminalInfoUrl = ApiConfig.arcServerUrl + "/app/fire/info/getTerminalInfo";
  public changeMobileCodeUrl = ApiConfig.arcServerUrl + "/app/user/common/changeMobileCode";
  public changeMobileUrl = ApiConfig.arcServerUrl + "/app/user/common/changeMobile";
  public getunDealCountUrl = ApiConfig.arcServerUrl + "/app/fire/index/unDealCount";
  public getDealCountUrl = ApiConfig.arcServerUrl + "/app/fire/index/dealCount";
  public getDefaultUrl = ApiConfig.arcServerUrl + "/app/fire/index/default";
  public getSiteInfoUrl = ApiConfig.arcServerUrl + "/app/fire/site/getSiteInfo";
  public getSiteListUrl = ApiConfig.arcServerUrl + "/app/fire/site/getSiteList";
  public getDeviceListUrl = ApiConfig.arcServerUrl + "/app/fire/site/getDeviceList";
  public getDeviceInfoUrl = ApiConfig.arcServerUrl + "/app/fire/site/getDeviceInfo";
  public getDeviceWarnListUrl = ApiConfig.arcServerUrl + "/app/fire/warn/getDeviceWarnList";
  public dealFeedbackUrl = ApiConfig.arcServerUrl + "/app/fire/warn/dealFeedback";
  public mapDetailUrl = ApiConfig.arcServerUrl + "/app/fire/index/mapData";
  public warnHistoryUrl = ApiConfig.arcServerUrl + "/app/fire/warn/getDeviceWarnHistoryList";
  public sendSocketMessageUrl = ApiConfig.arcServerUrl + "/app/sendSocketMessage ";
  public mqttRemoveWarningUrl = ApiConfig.arcServerUrl + "/app/mqttRemoveWarning";
  public getAlarmCountUrl = ApiConfig.arcServerUrl + "/app/fire/warn/getWarnCount";
  public findDeviceUrl = ApiConfig.arcServerUrl + "/app/fire/site/inquireDeviceList";

  /*T
   * postUrl 地址
   * data 数据
   * successFn 成功回调
   * */
  httpPost(postUrl: string, data = null, successFn,haveLoading = 1) {
    if(haveLoading){
      //this.alertMsgService.showLoading();
    }
    //http请求头部
    let headers = new HttpHeaders().set('Content-Type', 'application/x-www-form-urlencoded;charset=UTF-8');
    if(localStorage.getItem('token')){
      let token = localStorage.getItem('token');
      headers = new HttpHeaders().set('Content-Type', 'application/x-www-form-urlencoded;charset=UTF-8').set('Authorization', 'Bearer ' + token);
    }
    let options = { headers: headers};
    console.log(options);
    // 发送http请求
    return this.http.post<HttpResponse<any>>(postUrl, this.getWwwFormData(data), options).subscribe(successFn, this.errorFn, this.completeFn);
  }

  httpGet(getUrl:string,successFn){
    //this.alertMsgService.showLoading();
    //http请求头部
    let headers = new HttpHeaders().set('Content-Type', 'application/x-www-form-urlencoded;charset=UTF-8');
    if(localStorage.getItem('token')){
      let token = localStorage.getItem('token');
      headers = new HttpHeaders().set('Content-Type', 'application/x-www-form-urlencoded;charset=UTF-8').set('Authorization', 'Bearer ' + token);
    }
    let options = {headers: headers};
    // 发送http请求
    return this.http.get<HttpResponse<any>>(getUrl, options).subscribe(successFn, this.errorFn, this.completeFn);
  }


  //获取x-www-form-urlencoded格式的数据
  private getWwwFormData(data) {
    if (data == null) {
      return null;
    }
    let dataStr = "";
    for (let key in data) {
      dataStr += key + "=" + data[key] + "&";
    }
    dataStr = dataStr.substring(0, dataStr.length - 1);
    return dataStr;
  }


  //http调用错误
  private errorFn = (error) => {
    this.alertMsgService.closeLoading();
    let activeNav: NavController = this.app.getActiveNav();
    if(error.status == 401){
      let toast = this.alertMsgService.showMsg("登录超时，请重新登录");
      localStorage.removeItem("token");
      localStorage.removeItem("userInfo");
      activeNav.push(LoginPage, null, null, () => {
        this.app.getRootNav().setRoot(LoginPage);
      })
    }
    else if(error.status == 403){
      let toast = this.alertMsgService.showMsg("您没有此操作权限");
    }
    else{
      let toast = this.alertMsgService.showMsg("网络繁忙，请稍后重试");
    }
    return error;
  };

  //http调用结束
  private completeFn = () => {
    //todo:写个加载转圈圈结束

  }
}
