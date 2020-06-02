package com.helianshe.bullish;


import java.util.List;

public class StartConfigBean   {

    private List<BottomMenuBean> bottom_menu;

    public List<BottomMenuBean> getBottom_menu() {
        return bottom_menu;
    }

    public void setBottom_menu(List<BottomMenuBean> bottom_menu) {
        this.bottom_menu = bottom_menu;
    }

    public static class BottomMenuBean {
        /**
         * id : 1
         * name : 首页
         * target :
         * type : native
         */

        private String id;
        private String name;
        private String target;
        private String type;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTarget() {
            return target;
        }

        public void setTarget(String target) {
            this.target = target;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
