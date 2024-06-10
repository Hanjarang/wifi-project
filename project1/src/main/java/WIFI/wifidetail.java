package WIFI;

import java.text.DecimalFormat;

public class wifidetail {
    private String xSwifiMgrNo;
    private String xSwifiWrdoFc;
    private String xSwifiMainNm;
    private String xSwifiAdres1;
    private String xSwifiAdres2;
    private String xSwifiInstlFloor;
    private String xSwifiInstlTy;
    private String xSwifiInstlMby;
    private String xSwifiSvcSe;
    private String xSwifiCmcwr;
    private int xSwifiCnstcYear;
    private String xSwifiInoutDoor;
    private String xSwifiRemars3;
    private double lat;
    private double lnt;
    private String workDttm;

    // 거리를 계산하는 메서드
    public double getDistance(double userLat, double userLnt) {
        final int R = 6371; // 지구의 반지름 (킬로미터)
        double latDistance = Math.toRadians(userLat - this.lat);
        double lntDistance = Math.toRadians(userLnt - this.lnt);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(this.lat)) * Math.cos(Math.toRadians(userLat))
                * Math.sin(lntDistance / 2) * Math.sin(lntDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    // 거리를 포맷팅하여 반환하는 메서드
    public String formatDistance(double userLat, double userLnt) {
        DecimalFormat df = new DecimalFormat("#.####");
        return df.format(getDistance(userLat, userLnt));
    }

    // Getter와 Setter 메서드
    public String getxSwifiMgrNo() {
        return xSwifiMgrNo;
    }

    public void setxSwifiMgrNo(String xSwifiMgrNo) {
        this.xSwifiMgrNo = xSwifiMgrNo;
    }

    public String getxSwifiWrdoFc() {
        return xSwifiWrdoFc;
    }

    public void setxSwifiWrdoFc(String xSwifiWrdoFc) {
        this.xSwifiWrdoFc = xSwifiWrdoFc;
    }

    public String getxSwifiMainNm() {
        return xSwifiMainNm;
    }

    public void setxSwifiMainNm(String xSwifiMainNm) {
        this.xSwifiMainNm = xSwifiMainNm;
    }

    public String getxSwifiAdres1() {
        return xSwifiAdres1;
    }

    public void setxSwifiAdres1(String xSwifiAdres1) {
        this.xSwifiAdres1 = xSwifiAdres1;
    }

    public String getxSwifiAdres2() {
        return xSwifiAdres2;
    }

    public void setxSwifiAdres2(String xSwifiAdres2) {
        this.xSwifiAdres2 = xSwifiAdres2;
    }

    public String getxSwifiInstlFloor() {
        return xSwifiInstlFloor;
    }

    public void setxSwifiInstlFloor(String xSwifiInstlFloor) {
        this.xSwifiInstlFloor = xSwifiInstlFloor;
    }

    public String getxSwifiInstlTy() {
        return xSwifiInstlTy;
    }

    public void setxSwifiInstlTy(String xSwifiInstlTy) {
        this.xSwifiInstlTy = xSwifiInstlTy;
    }

    public String getxSwifiInstlMby() {
        return xSwifiInstlMby;
    }

    public void setxSwifiInstlMby(String xSwifiInstlMby) {
        this.xSwifiInstlMby = xSwifiInstlMby;
    }

    public String getxSwifiSvcSe() {
        return xSwifiSvcSe;
    }

    public void setxSwifiSvcSe(String xSwifiSvcSe) {
        this.xSwifiSvcSe = xSwifiSvcSe;
    }

    public String getxSwifiCmcwr() {
        return xSwifiCmcwr;
    }

    public void setxSwifiCmcwr(String xSwifiCmcwr) {
        this.xSwifiCmcwr = xSwifiCmcwr;
    }

    public int getxSwifiCnstcYear() {
        return xSwifiCnstcYear;
    }

    public void setxSwifiCnstcYear(int xSwifiCnstcYear) {
        this.xSwifiCnstcYear = xSwifiCnstcYear;
    }

    public String getxSwifiInoutDoor() {
        return xSwifiInoutDoor;
    }

    public void setxSwifiInoutDoor(String xSwifiInoutDoor) {
        this.xSwifiInoutDoor = xSwifiInoutDoor;
    }

    public String getxSwifiRemars3() {
        return xSwifiRemars3;
    }

    public void setxSwifiRemars3(String xSwifiRemars3) {
        this.xSwifiRemars3 = xSwifiRemars3;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLnt() {
        return lnt;
    }

    public void setLnt(double lnt) {
        this.lnt = lnt;
    }

    public String getWorkDttm() {
        return workDttm;
    }

    public void setWorkDttm(String workDttm) {
        this.workDttm = workDttm;
    }

}

