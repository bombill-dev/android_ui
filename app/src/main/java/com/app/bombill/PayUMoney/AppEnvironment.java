package com.app.bombill.PayUMoney;



public enum AppEnvironment {

    SANDBOX {
        @Override
        public String merchant_Key() {
//            return "1bicRQkP";
            return "l0oWoGLF";
        }

        @Override
        public String merchant_ID() {
//            return "7018307";
            return "7045830";
        }

        @Override
        public String furl() {
            return "https://www.payumoney.com/mobileapp/payumoney/failure.php";
        }

        @Override
        public String surl() {
            return "https://www.payumoney.com/mobileapp/payumoney/success.php";
        }

        @Override
        public String salt() {
//            return "0QWH8RRX2Q";
            return "I3Q1FZBRQ6";
        }

        @Override
        public boolean debug() {
            return true;
        }
    },
    PRODUCTION {
        @Override
        public String merchant_Key() {
            return "l0oWoGLF";
        }
        @Override
        public String merchant_ID() {
            return "7045830";
        }
        @Override
        public String furl() {
            return "https://www.payumoney.com/mobileapp/payumoney/failure.php";
        }

        @Override
        public String surl() {
            return "https://www.payumoney.com/mobileapp/payumoney/success.php";
        }

        @Override
        public String salt() {
            return "I3Q1FZBRQ6";
        }

        @Override
        public boolean debug() {
            return false;
        }
    };

    public abstract String merchant_Key();

    public abstract String merchant_ID();

    public abstract String furl();

    public abstract String surl();

    public abstract String salt();

    public abstract boolean debug();


}
