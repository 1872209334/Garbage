<template>

    <div style="height: 100%;">
        <div style="height: 100%; width:calc(100% - 185px); background-color: #F0F2F5; float: left;">
            <div class="pageBox">
                <div class="myNavigation">
                    <el-breadcrumb separator="/" style="display: inline-block">
                        <el-breadcrumb-item>
                            <span style="font-size: 16px;color:#303313;">项目管理</span>
                        </el-breadcrumb-item>
                        <el-breadcrumb-item>
                            <span style="color: #111111;font-size: 14px;color:#606266;">无线终端详情</span>
                        </el-breadcrumb-item>
                    </el-breadcrumb>
                </div>
                <div style="width:128px; height:34px; margin:auto 20px auto auto;" >
                    <!--<div style="width:56px; float: left; height:34px;">-->
                        <!--<el-button class="v-1-0-0-Button" @click="getInfo">查询</el-button>-->
                    <!--</div>-->

                    <div style="width:56px; float: right; height:34px; margin-left: 16px; ">
                        <!--<el-button class="v-1-0-0-Button-Minor" @click="goBack">返回</el-button>-->
                        <el-button class="v-1-0-0-Button-Minor" @click="goBack">关闭</el-button>
                    </div>
                </div>
                <div style="display: inline-block; height:40px;float: right;margin: 0 16px 6px 0;">
                    <!--<el-button class="v-1-0-0-Button" icon="el-icon-setting" @click="checkTerminalAlarm">告警设置</el-button>-->
                </div>

                <div style=" margin: 24px 16px 1% 16px;">
                    <table style="width: 100%; font-size: 14px;">
                        <tr>
                            <td width="145px" style="background-color: #F6F7FB;">
                                项&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                目 ：</td>
                            <!--<td class="pricrTd">{{equipInfo.siteLabel}}</td>-->
                            <td class="pricrTd">{{routeParam.siteName}}</td>
                            <td width="145px" style="background-color: #F6F7FB;">
                                设&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                备 ：</td>
                            <!--<td class="pricrTd">无线终端</td>-->
                            <td class="pricrTd">{{routeParam.type}}</td>
                            <td width="145px" style="background-color: #F6F7FB;">

                            编&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            号 ：</td>
                            <td class="pricrTd">{{zipperList[0].deviceId}}</td>
                        </tr>

                        <!--<tr v-if="this.routeParam.type=='组合式探测器' || this.routeParam.type=='剩余电流' ">-->
                            <!--<td style="background-color: #F6F7FB;">-->
                                <!--类&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-->
                                <!--型 ：</td>-->
                            <!--<td class="pricrTd">{{equipInfo.type}}</td>-->

                            <!--<td style="background-color: #F6F7FB;">-->
                                <!--型&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-->
                                <!--号 ：</td>-->
                            <!--<td class="pricrTd">-->
                                <!--{{equipInfo.equipmentType}}-->
                            <!--</td>-->
                            <!--<td style="background-color: #F6F7FB;letter-spacing: 0.2px;">-->
                                <!--硬&nbsp;&nbsp;件&nbsp;&nbsp;版&nbsp;&nbsp;本 ：</td>-->
                            <!--<td class="pricrTd">{{equipInfo.boardVersion}}</td>-->
                        <!--</tr>-->
                        <!--<tr v-if="this.routeParam.type=='电弧探测器' ">-->
                            <!--<td style="background-color: #F6F7FB;">-->
                                <!--类&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-->
                                <!--型 ：</td>-->
                            <!--<td class="pricrTd">{{equipInfo.type}}</td>-->

                            <!--<td style="background-color: #F6F7FB;">-->
                                <!--型&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-->
                                <!--号 ：</td>-->
                            <!--<td class="pricrTd">-->
                                <!--{{equipInfo.equipmentType='@'?"AFDD1000":equipInfo.equipmentType}}-->
                            <!--</td>-->
                            <!--<td style="background-color: #F6F7FB;letter-spacing: 0.2px;">-->
                                <!--软&nbsp;&nbsp;件&nbsp;&nbsp;版&nbsp;&nbsp;本 ：</td>-->
                            <!--<td class="pricrTd">{{equipInfo.softVersion}}</td>-->
                        <!--</tr>-->
                        <tr>
                            <!--<td style="background-color: #F6F7FB;letter-spacing: 0.2px;">-->
                                <!--发&nbsp;&nbsp;行&nbsp;&nbsp;时&nbsp;&nbsp;间 ：</td>-->
                            <!--<td class="pricrTd">{{equipInfo.releaseData}}</td>-->
                            <!--<td style="background-color: #F6F7FB;letter-spacing: 3px;padding-left: 3px;">产品序列号：</td>-->
                            <!--<td class="pricrTd" >{{equipInfo.equipmentProductionSequenceNumber}}</td>-->
                            <!--<td style="background-color: #F6F7FB;letter-spacing: 3px;padding-left: 3px;">I&nbsp;C&nbsp;C&nbsp;I&nbsp;D：</td>-->
                            <!--<td  class="pricrTd" >{{equipInfo.nbChipSerialNumber}}</td>-->
                            <!--<td style="background-color: #F6F7FB;">-->
                                <!--备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-->
                                <!--注 ：</td>-->
                            <!--<td  class="pricrTd" style="padding: 0;text-indent: 0">-->
                                <!--<input type="text" v-model="equipInfo.equip_place" :disabled="editPlace" style="width: calc(100% - 30px);border: 0;height: 100%;text-indent: 10px;overflow: hidden;text-overflow: ellipsis;white-space: nowrap" />-->
                                <!--<span style="cursor: pointer; color: #2d8cf0; font-size: 10px;display: inline-block" @click="changePlace"> {{placeBtn}}</span>-->
                            <!--</td>-->
                        </tr>

                        <!--<tr  v-if="this.routeParam.type=='电弧探测器'">-->
                            <!--<td style="background-color: #F6F7FB;">-->
                                <!--备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-->
                                <!--注 ：</td>-->
                            <!--<td  class="pricrTd" style="padding: 0;text-indent: 0">-->
                                <!--<input type="text" v-model="equipInfo.equip_place" :disabled="editPlace" style="width: calc(100% - 30px);border: 0;height: 100%;text-indent: 10px;overflow: hidden;text-overflow: ellipsis;white-space: nowrap" />-->
                                <!--<span style="cursor: pointer; color: #2d8cf0; font-size: 10px;display: inline-block" @click="changePlace"> {{placeBtn}}</span>-->
                            <!--</td>-->
                            <!--<td style="background-color: #F6F7FB;letter-spacing: 1.5px;padding-left: 3px;">-->
                                <!--描&nbsp;&nbsp;&nbsp;述&nbsp;&nbsp;&nbsp;符 ：</td>-->
                            <!--<td class="pricrTd" style="padding: 0;text-indent: 0">-->
                                <!--<input type="text" v-model="equipInfo.message" :disabled="editMsg" style="width: calc(100% - 30px);border: 0;height: 100%;text-indent: 10px;overflow: hidden;text-overflow: ellipsis;white-space: nowrap" />-->
                                <!--<span style="cursor: pointer; color: #2d8cf0; font-size: 10px;display: inline-block" @click="setMsg(equipInfo.message)"> {{msgBtn}}</span>-->
                            <!--</td>-->
                            <!--<td style="background-color: #F6F7FB;">单位电弧次数：</td>-->
                            <!--<td colspan="3" class="pricrTd">{{equipInfo.cnt}}</td>-->
                            <!--<td style="background-color: #F6F7FB;letter-spacing: 0.2px;" width="145px">-->
                            <!--温&nbsp;&nbsp;度&nbsp;&nbsp;上&nbsp;&nbsp;限 ：</td>-->
                            <!--<td class="pricrTd" style="padding: 0;text-indent: 0">-->
                                <!--<input type="text" v-model="equipInfo.temLimit0" :disabled="editTemLimit0" style="width: calc(100% - 30px);border: 0;height: 100%;text-indent: 10px;overflow: hidden;text-overflow: ellipsis;white-space: nowrap" />-->
                                <!--<span style="cursor: pointer; color: #2d8cf0; font-size: 10px;display: inline-block" @click="setTemLimit0(equipInfo.temLimit0)"> {{temLimit0Btn}}</span>-->
                            <!--</td>-->
                            <!--<td style="background-color: #F6F7FB;letter-spacing: 1.5px;padding-left: 3px;">-->
                                <!--灵&nbsp;&nbsp;&nbsp;敏&nbsp;&nbsp;&nbsp;度 ：</td>-->
                            <!--<td class="pricrTd" style="padding: 0;text-indent: 0">-->
                                <!--<input type="text" v-model="equipInfo.arcWarningSensitivity" :disabled="editArcWarningSensitivity" style="width: calc(100% - 30px);border: 0;height: 100%;text-indent: 10px;overflow: hidden;text-overflow: ellipsis;white-space: nowrap" />-->
                                <!--<span style="cursor: pointer; color: #2d8cf0; font-size: 10px;display: inline-block" @click="setArcWarningSensitivity(equipInfo.arcWarningSensitivity)"> {{arcWarningSensitivityBtn}}</span>-->

                            <!--</td>-->
                        <!--</tr>-->

                        <!--<tr  v-if="this.routeParam.type=='电弧探测器'">-->
                        <tr >
                            <!--<td style="background-color: #F6F7FB;">-->
                            <!--备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-->
                            <!--注 ：</td>-->
                            <!--<td  class="pricrTd" style="padding: 0;text-indent: 0">-->
                            <!--<input type="text" v-model="equipInfo.equip_place" :disabled="editPlace" style="width: calc(100% - 30px);border: 0;height: 100%;text-indent: 10px;overflow: hidden;text-overflow: ellipsis;white-space: nowrap" />-->
                            <!--<span style="cursor: pointer; color: #2d8cf0; font-size: 10px;display: inline-block" @click="changePlace"> {{placeBtn}}</span>-->
                            <!--</td>-->

                            <td style="background-color: #F6F7FB;" width="145px">
                                经&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                度 ：</td>
                            <td class="pricrTd" style="padding: 0;text-indent: 0">
                                <input type="text" v-model="equipInfo.lngBmap" :disabled="editLng" style="width: calc(100% - 30px);border: 0;height: 100%;text-indent: 10px;overflow: hidden;text-overflow: ellipsis;white-space: nowrap" />
                                <span style="cursor: pointer; color: #2d8cf0; font-size: 10px;display: inline-block" @click="setLng(equipInfo.lngBmap)"> {{lngBtn}}</span>
                            </td>
                            <td style="background-color: #F6F7FB;" width="145px">
                                纬&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                度 ：</td>
                            <td colspan="3" class="pricrTd" style="padding: 0;text-indent: 0">
                                <input type="text" v-model="equipInfo.latBmap" :disabled="editLat" style="width: calc(100% - 30px);border: 0;height: 100%;text-indent: 10px;overflow: hidden;text-overflow: ellipsis;white-space: nowrap" />
                                <span style="cursor: pointer; color: #2d8cf0; font-size: 10px;display: inline-block" @click="setLat(equipInfo.latBmap)"> {{latBtn}}</span>
                            </td>
                        </tr>
                        <!--<tr  v-if="this.routeParam.type=='组合式探测器' || this.routeParam.type=='剩余电流' ">-->
                            <!--<td style="background-color: #F6F7FB;">-->
                                <!--备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-->
                                <!--注 ：</td>-->
                            <!--<td  class="pricrTd" style="padding: 0;text-indent: 0">-->
                                <!--<input type="text" v-model="equipInfo.equip_place" :disabled="editPlace" style="width: calc(100% - 30px);border: 0;height: 100%;text-indent: 10px;overflow: hidden;text-overflow: ellipsis;white-space: nowrap" />-->
                                <!--<span style="cursor: pointer; color: #2d8cf0; font-size: 10px;display: inline-block" @click="changePlace"> {{placeBtn}}</span>-->
                            <!--</td>-->
                            <!--<td style="background-color: #F6F7FB;letter-spacing: 1.5px;padding-left: 3px;">-->
                                <!--描&nbsp;&nbsp;&nbsp;述&nbsp;&nbsp;&nbsp;符 ：</td>-->
                            <!--<td class="pricrTd" style="padding: 0;text-indent: 0" >-->
                                <!--<input type="text" v-model="equipInfo.message" :disabled="editMsg" style="width: calc(100% - 30px);border: 0;height: 100%;text-indent: 10px;overflow: hidden;text-overflow: ellipsis;white-space: nowrap" />-->
                                <!--<span style="cursor: pointer; color: #2d8cf0; font-size: 10px;display: inline-block" @click="setMsg(equipInfo.message)"> {{msgBtn}}</span>-->
                            <!--</td>-->
                            <!--<td style="background-color: #F6F7FB;" width="145px">-->
                                <!--经&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-->
                                <!--度 ：</td>-->
                            <!--<td class="pricrTd" style="padding: 0;text-indent: 0">-->
                                <!--<input type="text" v-model="equipInfo.lngBmap" :disabled="editLng" style="width: calc(100% - 30px);border: 0;height: 100%;text-indent: 10px;overflow: hidden;text-overflow: ellipsis;white-space: nowrap" />-->
                                <!--<span style="cursor: pointer; color: #2d8cf0; font-size: 10px;display: inline-block" @click="setLng(equipInfo.lngBmap)"> {{lngBtn}}</span>-->
                            <!--</td>-->
                            <!--<td style="background-color: #F6F7FB;" width="145px">-->
                                <!--纬&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-->
                                <!--度 ：</td>-->
                            <!--<td  class="pricrTd" style="padding: 0;text-indent: 0">-->
                                <!--<input type="text" v-model="equipInfo.latBmap" :disabled="editLat" style="width: calc(100% - 30px);border: 0;height: 100%;text-indent: 10px;overflow: hidden;text-overflow: ellipsis;white-space: nowrap" />-->
                                <!--<span style="cursor: pointer; color: #2d8cf0; font-size: 10px;display: inline-block" @click="setLat(equipInfo.latBmap)"> {{latBtn}}</span>-->
                            <!--</td>-->
                        <!--</tr>-->

                        <tr>
                            <!--<td style="background-color: #F6F7FB;letter-spacing: 0.2px;" width="145px">-->
                                <!--温&nbsp;&nbsp;度&nbsp;&nbsp;上&nbsp;&nbsp;限 ：</td>-->
                            <!--<td class="pricrTd">{{equipInfo.temLimit0}}</td>-->

                            <!--<td style="background-color: #F6F7FB;" width="145px">-->
                                <!--经&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-->
                                <!--度 ：</td>-->
                            <!--<td class="pricrTd" style="padding: 0;text-indent: 0">-->
                                <!--<input type="text" v-model="equipInfo.lngBmap" :disabled="editLng" style="width: calc(100% - 30px);border: 0;height: 100%;text-indent: 10px;overflow: hidden;text-overflow: ellipsis;white-space: nowrap" />-->
                                <!--<span style="cursor: pointer; color: #2d8cf0; font-size: 10px;display: inline-block" @click="setLng(equipInfo.lngBmap)"> {{lngBtn}}</span>-->
                            <!--</td>-->
                            <!--<td style="background-color: #F6F7FB;" width="145px">-->
                                <!--纬&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-->
                                <!--度 ：</td>-->
                            <!--<td colspan="3" class="pricrTd" style="padding: 0;text-indent: 0">-->
                                <!--<input type="text" v-model="equipInfo.latBmap" :disabled="editLat" style="width: calc(100% - 30px);border: 0;height: 100%;text-indent: 10px;overflow: hidden;text-overflow: ellipsis;white-space: nowrap" />-->
                                <!--<span style="cursor: pointer; color: #2d8cf0; font-size: 10px;display: inline-block" @click="setLat(equipInfo.latBmap)"> {{latBtn}}</span>-->
                            <!--</td>-->
                        </tr>
                        <tr>
                            <!--<td style="background-color: #F6F7FB;letter-spacing: 0.2px;">-->
                                <!--服务器IP地址：</td>-->
                            <!--<td class="pricrTd" style="padding: 0;text-indent: 0">-->
                                <!--<input type="text" v-model="equipInfo.serverIpAddress" :disabled="editIp" style="width: calc(100% - 30px);border: 0;height: 100%;text-indent: 10px;overflow: hidden;text-overflow: ellipsis;white-space: nowrap" />-->
                                <!--<span style="cursor: pointer; color: #2d8cf0; font-size: 10px;display: inline-block" @click="setIp(equipInfo.serverIpAddress)"> {{ipBtn}}</span>-->
                            <!--</td>-->
                            <!--<td style="background-color: #F6F7FB;padding-left: 3px;">服务器端口号：</td>-->
                            <!--<td class="pricrTd" style="padding: 0;text-indent: 0">-->
                                <!--<input type="text" v-model="equipInfo.serverPortNumber" :disabled="editPort" style="width: calc(100% - 30px);border: 0;height: 100%;text-indent: 10px;overflow: hidden;text-overflow: ellipsis;white-space: nowrap" />-->
                                <!--<span style="cursor: pointer; color: #2d8cf0; font-size: 10px;display: inline-block" @click="setPort(equipInfo.serverPortNumber)"> {{portBtn}}</span>-->
                            <!--</td>-->
                            <!--<td style="background-color: #F6F7FB;">蜂鸣器使能：</td>-->
                            <!--<td class="pricrTd">-->
                                <!--<el-switch v-model="buzzerEnableValue" @change="setBuzzerEnable">-->
                                <!--</el-switch>-->
                            <!--</td>-->
                        </tr>
                        <!--<tr  v-if="this.routeParam.type=='组合式探测器' || this.routeParam.type=='剩余电流' ">-->
                            <!--<td style="background-color: #F6F7FB;letter-spacing: 0.2px;">-->
                                <!--软&nbsp;&nbsp;件&nbsp;&nbsp;版&nbsp;&nbsp;本 ：</td>-->
                            <!--<td class="pricrTd" colspan="5">{{equipInfo.softVersion}}</td>-->
                        <!--</tr>-->
                        <!--<tr  v-if="this.routeParam.type=='组合式探测器' || this.routeParam.type=='剩余电流' ">-->
                            <!--<td style="background-color: #F6F7FB;">-->
                                <!--A相电流上限阀值(A)</td>-->
                            <!--<td  class="pricrTd" style="padding: 0;text-indent: 0">-->
                                <!--<input type="text" v-model="equipInfo.aCurrentUpLimit" :disabled="editACurrentUpLimit" style="width: calc(100% - 30px);border: 0;height: 100%;text-indent: 10px;overflow: hidden;text-overflow: ellipsis;white-space: nowrap" />-->
                                <!--<span style="cursor: pointer; color: #2d8cf0; font-size: 10px;display: inline-block" @click="setACurrentUpLimit(equipInfo.aCurrentUpLimit)"> {{aCurrentUpLimitBtn}}</span>-->
                            <!--</td>-->
                            <!--<td style="background-color: #F6F7FB;letter-spacing: 1.5px;padding-left: 3px;">-->
                                <!--B相电流上限阀值(A)</td>-->
                            <!--<td class="pricrTd" style="padding: 0;text-indent: 0" >-->
                                <!--<input type="text" v-model="equipInfo.bCurrentUpLimit" :disabled="editBCurrentUpLimit" style="width: calc(100% - 30px);border: 0;height: 100%;text-indent: 10px;overflow: hidden;text-overflow: ellipsis;white-space: nowrap" />-->
                                <!--<span style="cursor: pointer; color: #2d8cf0; font-size: 10px;display: inline-block" @click="setBCurrentUpLimit(equipInfo.bCurrentUpLimit)"> {{bCurrentUpLimitBtn}}</span>-->
                            <!--</td>-->
                            <!--<td style="background-color: #F6F7FB;letter-spacing: 1.5px;padding-left: 3px;">-->
                                <!--C相电流上限阀值(A)</td>-->
                            <!--<td class="pricrTd" style="padding: 0;text-indent: 0" >-->
                                <!--<input type="text" v-model="equipInfo.cCurrentUpLimit" :disabled="editCCurrentUpLimit" style="width: calc(100% - 30px);border: 0;height: 100%;text-indent: 10px;overflow: hidden;text-overflow: ellipsis;white-space: nowrap" />-->
                                <!--<span style="cursor: pointer; color: #2d8cf0; font-size: 10px;display: inline-block" @click="setCCurrentUpLimit(equipInfo.cCurrentUpLimit)"> {{cCurrentUpLimitBtn}}</span>-->
                            <!--</td>-->
                        <!--</tr>-->
                        <!--<tr  v-if="this.routeParam.type=='组合式探测器' || this.routeParam.type=='剩余电流' ">-->
                            <!--<td style="background-color: #F6F7FB;">-->
                                <!--A相电压上限阀值(V)</td>-->
                            <!--<td  class="pricrTd" style="padding: 0;text-indent: 0">-->
                                <!--<input type="text" v-model="equipInfo.aVoltageUpLimit" :disabled="editAVoltageUpLimit" style="width: calc(100% - 30px);border: 0;height: 100%;text-indent: 10px;overflow: hidden;text-overflow: ellipsis;white-space: nowrap" />-->
                                <!--<span style="cursor: pointer; color: #2d8cf0; font-size: 10px;display: inline-block" @click="setAVoltageUpLimit(equipInfo.aVoltageUpLimit)"> {{aVoltageUpLimitBtn}}</span>-->
                            <!--</td>-->
                            <!--<td style="background-color: #F6F7FB;letter-spacing: 1.5px;padding-left: 3px;">-->
                                <!--B相电压上限阀值(V)</td>-->
                            <!--<td class="pricrTd" style="padding: 0;text-indent: 0" >-->
                                <!--<input type="text" v-model="equipInfo.bVoltageUpLimit" :disabled="editBVoltageUpLimit" style="width: calc(100% - 30px);border: 0;height: 100%;text-indent: 10px;overflow: hidden;text-overflow: ellipsis;white-space: nowrap" />-->
                                <!--<span style="cursor: pointer; color: #2d8cf0; font-size: 10px;display: inline-block" @click="setBVoltageUpLimit(equipInfo.bVoltageUpLimit)"> {{bVoltageUpLimitBtn}}</span>-->
                            <!--</td>-->
                            <!--<td style="background-color: #F6F7FB;letter-spacing: 1.5px;padding-left: 3px;">-->
                                <!--C相电压上限阀值(V)</td>-->
                            <!--<td class="pricrTd" style="padding: 0;text-indent: 0" >-->
                                <!--<input type="text" v-model="equipInfo.cVoltageUpLimit" :disabled="editCVoltageUpLimit" style="width: calc(100% - 30px);border: 0;height: 100%;text-indent: 10px;overflow: hidden;text-overflow: ellipsis;white-space: nowrap" />-->
                                <!--<span style="cursor: pointer; color: #2d8cf0; font-size: 10px;display: inline-block" @click="setCVoltageUpLimit(equipInfo.cVoltageUpLimit)"> {{cVoltageUpLimitBtn}}</span>-->
                            <!--</td>-->
                        <!--</tr>-->
                        <!--<tr  v-if="this.routeParam.type=='组合式探测器' || this.routeParam.type=='剩余电流' ">-->
                            <!--<td style="background-color: #F6F7FB;">-->
                                <!--A相电压下限阀值(V)</td>-->
                            <!--<td  class="pricrTd" style="padding: 0;text-indent: 0">-->
                                <!--<input type="text" v-model="equipInfo.aVoltageDownLimit" :disabled="editAVoltageDownLimit" style="width: calc(100% - 30px);border: 0;height: 100%;text-indent: 10px;overflow: hidden;text-overflow: ellipsis;white-space: nowrap" />-->
                                <!--<span style="cursor: pointer; color: #2d8cf0; font-size: 10px;display: inline-block" @click="setAVoltageDownLimit(equipInfo.aVoltageDownLimit)"> {{aVoltageDownLimitBtn}}</span>-->
                            <!--</td>-->
                            <!--<td style="background-color: #F6F7FB;letter-spacing: 1.5px;padding-left: 3px;">-->
                                <!--B相电压下限阀值(V)</td>-->
                            <!--<td class="pricrTd" style="padding: 0;text-indent: 0" >-->
                                <!--<input type="text" v-model="equipInfo.bVoltageDownLimit" :disabled="editBVoltageDownLimit" style="width: calc(100% - 30px);border: 0;height: 100%;text-indent: 10px;overflow: hidden;text-overflow: ellipsis;white-space: nowrap" />-->
                                <!--<span style="cursor: pointer; color: #2d8cf0; font-size: 10px;display: inline-block" @click="setBVoltageDownLimit(equipInfo.bVoltageDownLimit)"> {{bVoltageDownLimitBtn}}</span>-->
                            <!--</td>-->
                            <!--<td style="background-color: #F6F7FB;letter-spacing: 1.5px;padding-left: 3px;">-->
                                <!--C相电压下限阀值(V)</td>-->
                            <!--<td class="pricrTd" style="padding: 0;text-indent: 0" >-->
                                <!--<input type="text" v-model="equipInfo.cVoltageDownLimit" :disabled="editCVoltageDownLimit" style="width: calc(100% - 30px);border: 0;height: 100%;text-indent: 10px;overflow: hidden;text-overflow: ellipsis;white-space: nowrap" />-->
                                <!--<span style="cursor: pointer; color: #2d8cf0; font-size: 10px;display: inline-block" @click="setCVoltageDownLimit(equipInfo.cVoltageDownLimit)"> {{cVoltageDownLimitBtn}}</span>-->
                            <!--</td>-->
                        <!--</tr>-->
                        <!--<tr  v-if="this.routeParam.type=='组合式探测器' || this.routeParam.type=='剩余电流' ">-->
                            <!--<td style="background-color: #F6F7FB;">-->
                                <!--0路温度门限&nbsp;&nbsp;&nbsp;&nbsp;(℃)</td>-->
                            <!--<td  class="pricrTd" style="padding: 0;text-indent: 0">-->
                                <!--<input type="text" v-model="equipInfo.temLimit0" :disabled="editTemLimit0" style="width: calc(100% - 30px);border: 0;height: 100%;text-indent: 10px;overflow: hidden;text-overflow: ellipsis;white-space: nowrap" />-->
                                <!--<span style="cursor: pointer; color: #2d8cf0; font-size: 10px;display: inline-block" @click="setTemLimit0(equipInfo.temLimit0)"> {{temLimit0Btn}}</span>-->
                            <!--</td>-->
                            <!--<td style="background-color: #F6F7FB;letter-spacing: 1.5px;padding-left: 3px;">-->
                                <!--1路温度门限&nbsp;&nbsp;&nbsp;&nbsp;(℃)</td>-->
                            <!--<td class="pricrTd" style="padding: 0;text-indent: 0" >-->
                                <!--<input type="text" v-model="equipInfo.temLimit1" :disabled="editTemLimit1" style="width: calc(100% - 30px);border: 0;height: 100%;text-indent: 10px;overflow: hidden;text-overflow: ellipsis;white-space: nowrap" />-->
                                <!--<span style="cursor: pointer; color: #2d8cf0; font-size: 10px;display: inline-block" @click="setTemLimit1(equipInfo.temLimit1)"> {{temLimit1Btn}}</span>-->
                            <!--</td>-->
                            <!--<td style="background-color: #F6F7FB;letter-spacing: 1.5px;padding-left: 3px;">-->
                                <!--2路温度门限&nbsp;&nbsp;&nbsp;&nbsp;(℃)</td>-->
                            <!--<td class="pricrTd" style="padding: 0;text-indent: 0" >-->
                                <!--<input type="text" v-model="equipInfo.temLimit2" :disabled="editTemLimit2" style="width: calc(100% - 30px);border: 0;height: 100%;text-indent: 10px;overflow: hidden;text-overflow: ellipsis;white-space: nowrap" />-->
                                <!--<span style="cursor: pointer; color: #2d8cf0; font-size: 10px;display: inline-block" @click="setTemLimit2(equipInfo.temLimit2)"> {{temLimit2Btn}}</span>-->
                            <!--</td>-->
                        <!--</tr>-->
                        <!--<tr  v-if="this.routeParam.type=='组合式探测器' || this.routeParam.type=='剩余电流' ">-->
                            <!--<td style="background-color: #F6F7FB;">-->
                                <!--3路温度门限&nbsp;&nbsp;&nbsp;&nbsp;(℃)</td>-->
                            <!--<td  class="pricrTd" style="padding: 0;text-indent: 0">-->
                                <!--<input type="text" v-model="equipInfo.temLimit3" :disabled="editTemLimit3" style="width: calc(100% - 30px);border: 0;height: 100%;text-indent: 10px;overflow: hidden;text-overflow: ellipsis;white-space: nowrap" />-->
                                <!--<span style="cursor: pointer; color: #2d8cf0; font-size: 10px;display: inline-block" @click="setTemLimit3(equipInfo.temLimit3)"> {{temLimit3Btn}}</span>-->
                            <!--</td>-->
                            <!--<td style="background-color: #F6F7FB;letter-spacing: 1.5px;padding-left: 3px;">-->
                                <!--4路温度门限&nbsp;&nbsp;&nbsp;&nbsp;(℃)</td>-->
                            <!--<td class="pricrTd" style="padding: 0;text-indent: 0" >-->
                                <!--<input type="text" v-model="equipInfo.temLimit4" :disabled="editTemLimit4" style="width: calc(100% - 30px);border: 0;height: 100%;text-indent: 10px;overflow: hidden;text-overflow: ellipsis;white-space: nowrap" />-->
                                <!--<span style="cursor: pointer; color: #2d8cf0; font-size: 10px;display: inline-block" @click="setTemLimit4(equipInfo.temLimit4)"> {{temLimit4Btn}}</span>-->
                            <!--</td>-->
                            <!--<td style="background-color: #F6F7FB;letter-spacing: 1.5px;padding-left: 3px;">-->
                                <!--漏电上限阀值&nbsp;&nbsp;(mA)</td>-->
                            <!--<td class="pricrTd" style="padding: 0;text-indent: 0" >-->
                                <!--<input type="text" v-model="equipInfo.leakageCurrentUpLimit" :disabled="editLeakageCurrentUpLimit" style="width: calc(100% - 30px);border: 0;height: 100%;text-indent: 10px;overflow: hidden;text-overflow: ellipsis;white-space: nowrap" />-->
                                <!--<span style="cursor: pointer; color: #2d8cf0; font-size: 10px;display: inline-block" @click="setLeakageCurrentUpLimit(equipInfo.leakageCurrentUpLimit)"> {{leakageCurrentUpLimitBtn}}</span>-->
                            <!--</td>-->
                        <!--</tr>-->

                    </table>
                </div>
                <!--<div style=" margin: 24px 16px 1% 16px;">-->
                    <!--<table style="width: 100%; font-size: 14px;">-->



                        <!--<tr v-if="this.routeParam.type=='组合式探测器' || this.routeParam.type=='剩余电流'">-->
                            <!--<td width="145px" style="background-color: #F6F7FB;">-->
                                <!--A&nbsp;&nbsp;相&nbsp;&nbsp;电&nbsp;&nbsp;流&nbsp;&nbsp;（A） ：</td>-->
                            <!--<td class="pricrTd">{{equipInfo.aCurrent}}</td>-->
                            <!--<td width="145px" style="background-color: #F6F7FB;">-->
                                <!--B&nbsp;&nbsp;相&nbsp;&nbsp;电&nbsp;&nbsp;流&nbsp;&nbsp;（A） ：</td>-->
                            <!--<td class="pricrTd">{{equipInfo.bCurrent}}</td>-->
                            <!--<td width="145px" style="background-color: #F6F7FB;">-->
                                <!--C&nbsp;&nbsp;相&nbsp;&nbsp;电&nbsp;&nbsp;流&nbsp;&nbsp;（A） ：</td>-->
                            <!--<td class="pricrTd">{{equipInfo.cCurrent}}</td>-->
                        <!--</tr>-->

                        <!--<tr v-if="this.routeParam.type=='组合式探测器' || this.routeParam.type=='剩余电流'">-->
                            <!--<td style="background-color: #F6F7FB;">-->
                                <!--A&nbsp;&nbsp;相&nbsp;&nbsp;电&nbsp;&nbsp;压&nbsp;&nbsp;（V） ：</td>-->
                            <!--<td class="pricrTd">{{equipInfo.aVoltage}}</td>-->

                            <!--<td style="background-color: #F6F7FB;">-->
                                <!--B&nbsp;&nbsp;相&nbsp;&nbsp;电&nbsp;&nbsp;压&nbsp;&nbsp;（V） ：</td>-->
                            <!--<td class="pricrTd">-->
                                <!--{{equipInfo.bVoltage}}-->
                            <!--</td>-->
                            <!--<td style="background-color: #F6F7FB;letter-spacing: 0.2px;">-->
                                <!--C&nbsp;&nbsp;相&nbsp;&nbsp;电&nbsp;&nbsp;压&nbsp;&nbsp;（V） ：</td>-->
                            <!--<td class="pricrTd">{{equipInfo.cVoltage}}</td>-->
                        <!--</tr>-->


                        <!--<tr  v-if="this.routeParam.type=='组合式探测器' || this.routeParam.type=='剩余电流' ">-->
                            <!--<td style="background-color: #F6F7FB;">-->
                                <!--温&nbsp;&nbsp;度&nbsp;&nbsp;值&nbsp;&nbsp;0&nbsp;&nbsp;（℃）：</td>-->
                            <!--<td class="pricrTd">{{equipInfo.tem0}}</td>-->
                            <!--<td style="background-color: #F6F7FB;">-->
                                <!--温&nbsp;&nbsp;度&nbsp;&nbsp;值&nbsp;&nbsp;1&nbsp;&nbsp;（℃） ：</td>-->
                            <!--<td class="pricrTd">{{equipInfo.tem1}}</td>-->
                            <!--<td style="background-color: #F6F7FB;">-->
                                <!--温&nbsp;&nbsp;度&nbsp;&nbsp;值&nbsp;&nbsp;2&nbsp;&nbsp;（℃）：</td>-->
                            <!--<td class="pricrTd">-->
                                <!--{{equipInfo.tem2}}-->
                            <!--</td>-->

                        <!--</tr>-->
                        <!--<tr v-if="this.routeParam.type=='组合式探测器' || this.routeParam.type=='剩余电流' ">-->
                            <!--<td style="background-color: #F6F7FB;">-->
                                <!--温&nbsp;&nbsp;度&nbsp;&nbsp;值&nbsp;&nbsp;3&nbsp;&nbsp;（℃） ：</td>-->
                            <!--<td class="pricrTd">{{equipInfo.tem3}}</td>-->
                            <!--<td style="background-color: #F6F7FB;">-->
                                <!--温&nbsp;&nbsp;度&nbsp;&nbsp;值&nbsp;&nbsp;4&nbsp;&nbsp;（℃） ：</td>-->
                            <!--<td class="pricrTd">{{equipInfo.tem4}}</td>-->
                            <!--<td style="background-color: #F6F7FB;">-->
                                <!--漏&nbsp;&nbsp;电&nbsp;&nbsp;流&nbsp;&nbsp;（mA）：</td>-->
                            <!--<td class="pricrTd">-->
                                <!--{{equipInfo.leakageCurrent}}-->
                            <!--</td>-->

                        <!--</tr>-->



                    <!--</table>-->
                <!--</div>-->

                <!--中控信息设备信息列表-->
                <div style="height:calc(100% - 146px);margin: 15px 16px 0 16px;border: 1px solid #F0F2F5;overflow: auto">
                    <el-table ref="multipleTable" height="100%" :data="zipperList"
                              tooltip-effect="dark" style="width: 100%" @selection-change="handleSelectionChange" :header-cell-style="tableStyle">

                        <el-table-column type="selection" width="48">
                        </el-table-column>

                        <el-table-column type="index" width="56" label="#">
                        </el-table-column>

                        <!--<el-table-column prop="unid" :formatter="formatNumber" label="编号">-->
                        <!--</el-table-column>-->

                        <!--<el-table-column prop="deviceId" label="设备编号" show-overflow-tooltip>-->
                        <!--</el-table-column>-->
                        <el-table-column prop="finishedSum" label="成品数" show-overflow-tooltip>
                        </el-table-column>
                        <el-table-column prop="defectiveSum" label="次品数" show-overflow-tooltip>
                        </el-table-column>
                        <el-table-column prop="totalCycles" label="总循环次数" show-overflow-tooltip>
                        </el-table-column>
                        <el-table-column prop="currentAngle" label="当前角度" show-overflow-tooltip>
                        </el-table-column>
                        <el-table-column prop="currentSpeed" label="当前速度" show-overflow-tooltip>
                        </el-table-column>
                        <el-table-column prop="speedSetting" label="速度设定" show-overflow-tooltip>
                        </el-table-column>
                        <el-table-column prop="createTime" label="创建时间" show-overflow-tooltip>
                        </el-table-column>

                        <!--<el-table-column prop="tempration" label="温度" v-if="equipType == '1'" :key="Math.random()">-->
                        <!--</el-table-column>-->

                        <!--<el-table-column prop="broken_num" label="电压/电流" v-if="equipType == '1'" :key="Math.random()">-->
                        <!--</el-table-column>-->

                        <el-table-column prop="isAlarm" :formatter="formatState" label="正常/告警"></el-table-column>
                        <!--<el-table-column fixed="right" label="操作">-->
                            <!--<template slot-scope="scope">-->
                                <!--<el-button-->
                                    <!--@click.native.prevent="checkTerminal(scope.row)"-->
                                    <!--type="text"-->
                                    <!--size="small">-->
                                    <!--<span style="color: #387EE8;">查看</span>-->
                                <!--</el-button>-->
                                <!--<el-button style="color: red;"-->
                                           <!--v-if="actionList.indexOf('deleteDevice')>-1"-->
                                           <!--@click.native.prevent="delTerminal(scope.row)"-->
                                           <!--type="text"-->
                                           <!--size="small">-->
                                    <!--<span>删除</span>-->
                                <!--</el-button>-->
                            <!--</template>-->
                        <!--</el-table-column>-->

                    </el-table>
                </div>
                <div style="float: right;margin-right: 13px;">
                    <el-pagination
                        @size-change="handleSizeChange"
                        @current-change="handleCurrentChange"
                        :page-size="common.pageSize"
                        :page-sizes="common.allSizes"
                        :current-page="common.page"
                        layout="total,sizes, prev, pager, next, jumper"
                        :total="common.total">
                    </el-pagination>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import http from '../../../common/http';
    import bus from '../../../common/bus';
    export default {
        name: "mqttInformation",
        data(){
            return{
                equipInput:"",
                tableStyle:{
                    'background-color':'#F6F7FB',
                    'font-weight':'400'
                },
                common:{
                    total:0,            //总数据量
                    pageSize:20,        //每页条数
                    page:1,              //页码
                    isPage:1,            //是否分页
                    allSizes:[10,20,30], //每页条数筛选
                    checkboxArr:[]       //多选框数组
                },
                zipperList: [], //终端列表
                routeParam:{},
                equipInfo:{},
                editPlace:true,
                editMsg:true,
                editLng:true,
                editLat:true,
                editNbNumber:true,
                editIccid:true,
                editIp:true,
                editPort:true,
                editLeakageCurrentUpLimit:true,
                editAVoltageUpLimit:true,
                editBVoltageUpLimit:true,
                editCVoltageUpLimit:true,
                editAVoltageDownLimit:true,
                editBVoltageDownLimit:true,
                editCVoltageDownLimit:true,
                editTemLimit0:true,
                editTemLimit1:true,
                editTemLimit2:true,
                editTemLimit3:true,
                editTemLimit4:true,
                editACurrentUpLimit:true,
                editBCurrentUpLimit:true,
                editCCurrentUpLimit:true,
                editArcWarningSensitivity:true,
                placeBtn:"修改",
                msgBtn:"修改",
                lngBtn:"修改",
                latBtn:"修改",
                nbNumberBtn:"修改",
                iccidBtn:"修改",
                ipBtn:"修改",
                portBtn:"修改",
                temLimit0Btn:"修改",
                temLimit1Btn:"修改",
                temLimit2Btn:"修改",
                temLimit3Btn:"修改",
                temLimit4Btn:"修改",
                aCurrentUpLimitBtn:"修改",
                bCurrentUpLimitBtn:"修改",
                cCurrentUpLimitBtn:"修改",
                aVoltageUpLimitBtn:"修改",
                bVoltageUpLimitBtn:"修改",
                cVoltageUpLimitBtn:"修改",
                aVoltageDownLimitBtn:"修改",
                bVoltageDownLimitBtn:"修改",
                cVoltageDownLimitBtn:"修改",
                leakageCurrentUpLimitBtn:"修改",
                arcWarningSensitivityBtn:"修改",
                buzzerEnableValue:false,
            }
        },
        created:function(){
            this.routeParam = this.$route.params;
            console.log(this.routeParam);
            this.getEquipInfo();
            this.getZipperInfo();
            bus.$on('efmClickEfmClick',(msg)=>{
                this.routeParam.id = msg.id;
                this.routeParam.efmName = msg.efmName;
                this.getEquipInfo();
                this.getZipperInfo();
            })
        },
        methods:{
            getInfo(){
                if(this.routeParam.type=='组合式探测器' || this.routeParam.type=='剩余电流' ){
                    this.queryMqtt2();
                    setTimeout(this.queryMqtt,3000);
                }
                else{
                    this.queryMqtt4();
                    setTimeout(this.queryMqtt3,3000);

                }

            },
            changePlace(){
                if(this.editPlace == true){
                    this.editPlace = false;
                    this.placeBtn = "确定";
                }
                else{
                    this.changeParam();
                }
            },
            setArcWarningSensitivity(content){
                if(this.editArcWarningSensitivity == true){
                    this.editArcWarningSensitivity = false;
                    this.arcWarningSensitivityBtn = "确定";
                }
                else{
                    this.setParams('arc_warning_sensitivity',content);
                }
            },
            setMsg(content){
                if(this.editMsg == true){
                    this.editMsg = false;
                    this.msgBtn = "确定";
                }
                else{
                    this.setParams('device_descriptor',content);
                }
            },
            setLeakageCurrentUpLimit(content){
                if(this.editLeakageCurrentUpLimit == true){
                    this.editLeakageCurrentUpLimit = false;
                    this.leakageCurrentUpLimitBtn = "确定";
                }
                else{
                    this.setParams('leakageCurrentUpLimit',content);
                }
            },
            setAVoltageDownLimit(content){
                if(this.editAVoltageDownLimit == true){
                    this.editAVoltageDownLimit = false;
                    this.aVoltageDownLimitBtn = "确定";
                }
                else{
                    this.setParams('AVoltageDownLimit',content);
                }
            },
            setBVoltageDownLimit(content){
                if(this.editBVoltageDownLimit == true){
                    this.editBVoltageDownLimit = false;
                    this.bVoltageDownLimitBtn = "确定";
                }
                else{
                    this.setParams('BVoltageDownLimit',content);
                }
            },
            setCVoltageDownLimit(content){
                if(this.editCVoltageDownLimit == true){
                    this.editCVoltageDownLimit = false;
                    this.cVoltageDownLimitBtn = "确定";
                }
                else{
                    this.setParams('CVoltageDownLimit',content);
                }
            },
            setAVoltageUpLimit(content){
                if(this.editAVoltageUpLimit == true){
                    this.editAVoltageUpLimit = false;
                    this.aVoltageUpLimitBtn = "确定";
                }
                else{
                    this.setParams('AVoltageUpLimit',content);
                }
            },
            setBVoltageUpLimit(content){
                if(this.editBVoltageUpLimit == true){
                    this.editBVoltageUpLimit = false;
                    this.bVoltageUpLimitBtn = "确定";
                }
                else{
                    this.setParams('BVoltageUpLimit',content);
                }
            },
            setCVoltageUpLimit(content){
                if(this.editCVoltageUpLimit == true){
                    this.editCVoltageUpLimit = false;
                    this.cVoltageUpLimitBtn = "确定";
                }
                else{
                    this.setParams('CVoltageUpLimit',content);
                }
            },
            setACurrentUpLimit(content){
                if(this.editACurrentUpLimit == true){
                    this.editACurrentUpLimit = false;
                    this.aCurrentUpLimitBtn = "确定";
                }
                else{
                    this.setParams('ACurrentUpLimit',content);
                }
            },
            setBCurrentUpLimit(content){
                if(this.editBCurrentUpLimit == true){
                    this.editBCurrentUpLimit = false;
                    this.bCurrentUpLimitBtn = "确定";
                }
                else{
                    this.setParams('BCurrentUpLimit',content);
                }
            },
            setCCurrentUpLimit(content){
                if(this.editCCurrentUpLimit == true){
                    this.editCCurrentUpLimit = false;
                    this.cCurrentUpLimitBtn = "确定";
                }
                else{
                    this.setParams('CCurrentUpLimit',content);
                }
            },
            setTemLimit0(content){
                if(this.editTemLimit0 == true){
                    this.editTemLimit0 = false;
                    this.temLimit0Btn = "确定";
                }
                else{
                    this.setParams('0temLimit',content);
                }
            },
            setTemLimit1(content){
                if(this.editTemLimit1 == true){
                    this.editTemLimit1 = false;
                    this.temLimit1Btn = "确定";
                }
                else{
                    this.setParams('1temLimit',content);
                }
            },
            setTemLimit2(content){
                if(this.editTemLimit2 == true){
                    this.editTemLimit2 = false;
                    this.temLimit2Btn = "确定";
                }
                else{
                    this.setParams('2temLimit',content);
                }
            },
            setTemLimit3(content){
                if(this.editTemLimit3 == true){
                    this.editTemLimit3 = false;
                    this.temLimit3Btn = "确定";
                }
                else{
                    this.setParams('3temLimit',content);
                }
            },
            setTemLimit4(content){
                if(this.editTemLimit4 == true){
                    this.editTemLimit4 = false;
                    this.temLimit4Btn = "确定";
                }
                else{
                    this.setParams('4temLimit',content);
                }
            },

            setMsg(content){
                if(this.editMsg == true){
                    this.editMsg = false;
                    this.msgBtn = "确定";
                }
                else{
                    this.setParams('device_descriptor',content);
                }
            },
            setLng(content){
                if(this.editLng == true){
                    this.editLng = false;
                    this.lngBtn = "确定";
                }
                else{
                    this.setParams('longitude',content);
                }
            },
            setLat(content){
                if(this.editLat == true){
                    this.editLat = false;
                    this.latBtn = "确定";
                }
                else{
                    this.setParams('latitude',content);
                }
            },
            setNbNumber(content){
                if(this.editNbNumber == true){
                    this.editNbNumber = false;
                    this.nbNumberBtn = "确定";
                }
                else{
                    this.setParams('nb_phone_number',content);
                }
            },
            setIccid(content){
                if(this.editIccid == true){
                    this.editIccid = false;
                    this.iccidBtn = "确定";
                }
                else{
                    this.setParams('nb_chip_serial_number',content);
                }
            },
            setIp(content){
                if(this.editIp == true){
                    this.editIp = false;
                    this.ipBtn = "确定";
                }
                else{
                    let ipArr = content.split(".");
                    if(ipArr.length != 4){
                        this.$commonFn.showTip("格式错误", 2);
                        return;
                    }
                    let ipStr = "";
                    for (let i = 0;i < ipArr.length;i++){
                        let str = parseInt(ipArr[i]).toString(16);
                        if(str.length == 1){
                            str = "0" + str;
                        }
                        ipStr += str;
                    }
                    ipStr = ipStr.toUpperCase();
                    let url = this.mqttIssuingInstructionsUrl;
                    let data = {
                        message: 'server_ip_address',
                        qos: 1,
                        ID: "01" + this.equipInfo.id,
                        type: 3,
                        extraData:ipStr
                    };
                    this.apiPost(url, data).then((res) => {
                        this.$commonFn.showTip("设置成功", 1);
                        this.editIp = true;
                        this.ipBtn = "修改";
                        setTimeout(this.getEquipInfo,5000);
                    }, (err) => {
                        console.log(err);
                        this.editIp = true;
                        this.ipBtn = "修改";
                    });
                }
            },
            setBuzzerEnable(){
                let url = this.mqttIssuingInstructionsUrl;
                let data = {
                    message: 'buzzer_enable',
                    qos: 1,
                    ID: "01" + this.equipInfo.id,
                    type: 3,
                    extraData:"0" + Number(!this.buzzerEnableValue)
                };
                this.apiPost(url, data).then((res) => {
                    setTimeout(this.getEquipInfo,5000);
                }, (err) => {

                });
            },
            setPort(content){
                if(this.editPort == true){
                    this.editPort = false;
                    this.portBtn = "确定";
                }
                else{
                    let port = content;
                    let dport = this.decimalToHexString(parseInt(port));
                    if(dport.length != 4){
                        dport = "0" + dport
                    }
                    let dport1 = dport.substring(0,2);
                    let dport2 = dport.substring(2,4);
                    dport = dport2 + dport1;
                    let url = this.mqttIssuingInstructionsUrl;
                    let data = {
                        message: 'server_port_number',
                        qos: 1,
                        ID: "01" + this.equipInfo.id,
                        type: 3,
                        extraData:dport
                    };
                    this.apiPost(url, data).then((res) => {
                        this.$commonFn.showTip("设置成功", 1);
                        this.editPort = true;
                        this.portBtn = "修改";
                        setTimeout(this.getEquipInfo,5000);
                    }, (err) => {
                        this.editPort = true;
                        this.portBtn = "修改";
                        console.log(err);
                    });
                }
            },
            decimalToHexString(number)
            {
                if (number < 0)
                {
                    number = 0xFFFFFFFF + number + 1;
                }
                return number.toString(16).toUpperCase();
            },
            changeParam(){
                let url = this.editEquipInfoUrl;
                let data = {
                    equipId:this.equipInfo.id,
                    equipPlace:this.equipInfo.equip_place
                };
                this.apiPost(url, data).then((res) => {
                    this.editPlace = true;
                    this.placeBtn = "修改";
                });
            },
            setParams(cmd,content){
                let url = this.setMqttIssuingInstructionsUrl;
                let data = {
                    message:cmd,
                    qos: 1,
                    ID: "01" + this.equipInfo.id,
                    type: 3,
                    extraData:content
                };
                this.apiPost(url, data).then((res) => {
                    if (cmd == 'device_descriptor') {
                        this.editMsg = true;
                        this.msgBtn = "修改";
                    }
                    else if(cmd == 'longitude'){
                        this.editLng = true;
                        this.lngBtn = "修改";
                    }
                    else if(cmd == 'latitude'){
                        this.editLat = true;
                        this.latBtn = "修改";
                    }
                    else if(cmd == 'nb_phone_number'){
                        this.editNbNumber = true;
                        this.nbNumberBtn = "修改";
                    }
                    else if(cmd == 'nb_chip_serial_number'){
                        this.editIccid = true;
                        this.iccidBtn = "修改";
                    }
                    else if(cmd == '0temLimit'){
                        this.editTemLimit0 = true;
                        this.temLimit0Btn = "修改";
                    }
                    else if(cmd == '1temLimit'){
                        this.editTemLimit1 = true;
                        this.temLimit1Btn = "修改";
                    }
                    else if(cmd == '2temLimit'){
                        this.editTemLimit2 = true;
                        this.temLimit2Btn = "修改";
                    }
                    else if(cmd == '3temLimit'){
                        this.editTemLimit3 = true;
                        this.temLimit3Btn = "修改";
                    }
                    else if(cmd == '4temLimit'){
                        this.editTemLimit4 = true;
                        this.temLimit4Btn = "修改";
                    }
                    else if(cmd == 'leakageCurrentUpLimit'){
                        this.editLeakageCurrentUpLimit = true;
                        this.leakageCurrentUpLimitBtn = "修改";
                    }
                    else if(cmd == 'AVoltageDownLimit'){
                        this.editAVoltageDownLimit = true;
                        this.aVoltageDownLimitBtn = "修改";
                    }
                    else if(cmd == 'BVoltageDownLimit'){
                        this.editBVoltageDownLimit = true;
                        this.bVoltageDownLimitBtn = "修改";
                    }
                    else if(cmd == 'CVoltageDownLimit'){
                        this.editCVoltageDownLimit = true;
                        this.cVoltageDownLimitBtn = "修改";
                    }
                    else if(cmd == 'AVoltageUpLimit'){
                        this.editAVoltageUpLimit = true;
                        this.aVoltageUpLimitBtn = "修改";
                    }
                    else if(cmd == 'BVoltageUpLimit'){
                        this.editBVoltageUpLimit = true;
                        this.bVoltageUpLimitBtn = "修改";
                    }
                    else if(cmd == 'CVoltageUpLimit'){
                        this.editCVoltageUpLimit = true;
                        this.cVoltageUpLimitBtn = "修改";
                    }
                    else if(cmd == 'ACurrentUpLimit'){
                        this.editACurrentUpLimit = true;
                        this.aCurrentUpLimitBtn = "修改";
                    }
                    else if(cmd == 'BCurrentUpLimit'){
                        this.editBCurrentUpLimit = true;
                        this.bCurrentUpLimitBtn = "修改";
                    }
                    else if(cmd == 'CCurrentUpLimit'){
                        this.editCCurrentUpLimit = true;
                        this.cCurrentUpLimitBtn = "修改";
                    }
                    else if(cmd == 'arc_warning_sensitivity'){
                        this.editArcWarningSensitivity = true;
                        this.arcWarningSensitivityBtn = "修改";
                    }
                    setTimeout(this.getEquipInfo,5000);
                });
            },
            handleSelectionChange(val) {
                this.common.checkboxArr = [];
                for (let i = 0;i < val.length;i++){
                    this.common.checkboxArr.push(val[i].id);
                }
            },
            formatNumber: function (row, column) {
                return row.serialNumber = parseInt(row.line_code) + 1 + "-" + row.addr;
            },
            formatState:function(row, column){
                if(row.isAlarm == 0){
                    return "正常";
                }else if(row.isAlarm == 1){
                    return "告警";
                }else if(row.isAlarm == 2){
                    return "故障";
                }else if(row.isAlarm == 3){
                    return "离线";
                }
            },
            handleSizeChange(val) {
                this.common.pageSize = val;
                this.common.page = 1;
                this.getZipperInfo();
            },
            handleCurrentChange(val) {
                this.common.page = val;
                this.getZipperInfo();
            },
            checkTerminal(data){
                console.log(data);
                this.$router.push({
                    name:'terminalInformation',
                    params:{
                        equipId:data.id,
                        efmId:this.routeParam.id,
                        efmName:this.routeParam.efmName,
                        siteLabel:this.efmInfo.getdeviceInfo.siteName?this.efmInfo.getdeviceInfo.siteName:this.efmInfo.getdeviceInfo.site_code
                    }
                });
            },
            getEquipInfo(){
                let url = this.getEquipInfoUrl;
                let data = {
                    equipId:this.routeParam.id
                };
                this.apiPost(url, data).then((res) => {

                    if(res.status === 200){
                        this.equipInfo = res.data;
                        // console.log(this.equipInfo)
                        // this.equipInfo.serialNumber = parseInt(this.equipInfo.line_code) + 1 + "-" + parseInt(this.equipInfo.addr);
                        // let typeArr = ['电弧探测器','组合式探测器','测温式探测器'];
                        // this.equipInfo.type = typeArr[this.equipInfo.type];
                        // this.equipInfo.siteLabel = this.equipInfo.siteName?this.equipInfo.siteName:this.equipInfo.siteCode;
                        // if(this.equipInfo.niNameDevice){
                        //     this.equipInfo.efmLabel = this.equipInfo.niNameDevice;
                        // }
                        // else if(this.equipInfo.specificatorDevice){
                        //     this.equipInfo.efmLabel = this.equipInfo.specificatorDevice;
                        // }
                        // else{
                        //     this.equipInfo.efmLabel = parseInt(this.equipInfo.deviceCode,16);
                        // }
                        // this.buzzerEnableValue = !this.equipInfo.buzzerEnable;
                    }
                });
            },
            getZipperInfo(){
                let _loading = this.$commonFn.showLoading(2,'.efm-box');
                let url = this.getZipperInfoUrl;
                let data = {
                    deviceId:this.routeParam.id,
                    pageSize: this.common.pageSize,
                    currentPage: this.common.page
                };
                // if(this.equipInput.indexOf("-") > 0){
                //     data.parameterType = 1;
                //     let arr = this.equipInput.split('-');
                //     let arr1 = parseInt(arr[0]) - 1;
                //     //data.inquire = arr1 + '-' + arr[1];
                // }
                // else{
                //     data.parameterType = 0;
                // }
                this.apiPost(url, data).then((res) => {
                    _loading.close();
                    if(res.status === 200){
                        this.zipperList= res.data.zipperList;
                        this.common.total = res.data.zipperListCount;
                    }else{
                        this.$commonFn.showTip(res.message,3);
                    }
                },(err) =>{
                    _loading.close();
                });
                // _loading.close();
            },
            checkTerminalAlarm(){
                this.$router.push(
                    {
                        name:'terminalAlarm',
                        params:{
                            equipId:this.routeParam.equipId,
                            efmId:this.routeParam.efmId,
                            efmName:this.routeParam.efmName,
                            siteLabel:this.routeParam.efmName.siteLabel
                        }
                    }
                );
            },
            //查询mqtt参数
            queryMqtt(){
                let url = this.mqttIssuingInstructionsUrl;
                let data = {
                    message: 'equipment_category,equipment_type,arc_number,arc_time_range,hardware_version,issue_time,equipment_production_sequence_number,software_version,tem0,tem1,tem3,tem3,tem4,leakageCurrent,CCurrent,BCurrent,ACurrent,AVoltage,BVoltage,CVoltage',
                    //message: 'equipment_type,arc_number,arc_time_range,temperature_warning_threshold,hardware_version,longitude,latitude',
                    qos: 1,
                    ID: "01" + this.equipInfo.id,
                    type: 5,
                    extraData:""
                };
                this.apiPost(url, data).then((res) => {
                    this.$commonFn.showTip("查询成功", 1);
                    setTimeout(this.getEquipInfo,2000);
                }, (err) => {
                    console.log(err);
                });
            },
            queryMqtt2(){
                let url = this.mqttIssuingInstructionsUrl;
                let data = {
                    message: 'longitude,latitude,device_descriptor,nb_chip_serial_number,nb_phone_number,server_ip_address,server_port_number,buzzer_enable,0temLimit,1temLimit,2temLimit,3temLimit,4temLimit,leakageCurrentUpLimit,AVoltageUpLimit,BVoltageUpLimit,CVoltageUpLimit,' +
                        'AVoltageDownLimit,BVoltageDownLimit,CVoltageDownLimit,ACurrentUpLimit,BCurrentUpLimit,CCurrentUpLimit',
                    qos: 1,
                    ID: "01" + this.equipInfo.id,
                    type: 5,
                    extraData:""
                };
                this.apiPost(url, data).then((res) => {
                    //this.$commonFn.showTip("查询成功", 1);
                }, (err) => {
                    console.log(err);
                });
            },
            queryMqtt3(){
                let url = this.mqttIssuingInstructionsUrl;
                let data = {
                    //message: 'software_version',
                    message: 'longitude,latitude,device_descriptor,nb_chip_serial_number,server_ip_address,server_port_number,equipment_production_sequence_number,buzzer_enable',
                    //message: 'equipment_type,arc_number,arc_time_range,temperature_warning_threshold,hardware_version,longitude,latitude',
                    qos: 1,
                    ID: "01" + this.equipInfo.id,
                    type: 5,
                    extraData:""
                };
                this.apiPost(url, data).then((res) => {
                    this.$commonFn.showTip("查询成功", 1);
                   // this.getEquipInfo();
                    setTimeout(this.getEquipInfo,2000);
                }, (err) => {
                    console.log(err);
                });
            },
            queryMqtt4(){
                let url = this.mqttIssuingInstructionsUrl;
                let data = {
                    message: 'software_version,arc_warning_sensitivity,0temLimit,equipment_type',

                    qos: 1,
                    ID: "01" + this.equipInfo.id,
                    type: 5,
                    extraData:""
                };
                this.apiPost(url, data).then((res) => {
                    //this.$commonFn.showTip("查询成功", 1);
                }, (err) => {
                    console.log(err);
                });
            },
            goBack(){
                this.$router.push(
                    {
                        name:'mqttDeviceList',
                        params:{
                            siteId:this.routeParam.site_id,
                            equipmentTypeValue:this.routeParam.equipmentTypeValue,
                            inputBoxValue:this.routeParam.inputBoxValue,
                            page:this.routeParam.page

                        }
                    }
                );
            }
        },
        mixins: [http]
    }

</script>

<style scoped>
    table{
        border: 1px solid rgb(216,220,228);
        border-collapse: collapse;
    }

    td{
        font-weight: 400;
        text-align: center;
        border: 1px solid rgb(216,220,228);
        height:38px;
    }

    .pricrTd{
        font-weight: 400 !important;
        text-align: left !important;
        text-indent: 10px;
        width: 170px;
    }
    .buttonClass{

        margin-top:400px !important;
    }
</style>
