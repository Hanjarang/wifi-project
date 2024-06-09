package WIFI;

import java.util.List;

public class wifiinfo {
    private TbPublicWifiInfo TbPublicWifiInfo;

    public TbPublicWifiInfo getTbPublicWifiInfo() {
        return TbPublicWifiInfo;
    }

    public void setTbPublicWifiInfo(TbPublicWifiInfo tbPublicWifiInfo) {
        TbPublicWifiInfo = tbPublicWifiInfo;
    }

    public static class TbPublicWifiInfo {
        private int list_total_count;
        private List<wifidetail> row;

        public int getList_total_count() {
            return list_total_count;
        }

        public void setList_total_count(int list_total_count) {
            this.list_total_count = list_total_count;
        }

        public List<wifidetail> getRow() {
            return row;
        }

        public void setRow(List<wifidetail> row) {
            this.row = row;
        }
    }
}

