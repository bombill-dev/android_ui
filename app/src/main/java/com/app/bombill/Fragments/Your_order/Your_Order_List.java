package com.app.bombill.Fragments.Your_order;

public class Your_Order_List {
    private String Vendor_Order_Id;
    private String Order_date;
    private String Payment_status;
    private String Payment_method;
    private String Transaction_id;
    private String Order_status;
    private String Grand_total;
    private String Vendor_Name;

    public String getVendor_Order_Id() {
        return Vendor_Order_Id;
    }

    public String getOrder_date() {
        return Order_date;
    }

    public String getPayment_status() {
        return Payment_status;
    }

    public String getPayment_method() {
        return Payment_method;
    }

    public String getTransaction_id() {
        return Transaction_id;
    }

    public String getOrder_status() {
        return Order_status;
    }

    public String getGrand_total() {
        return Grand_total;
    }

    public String getVendor_Name() {
        return Vendor_Name;
    }

    public Your_Order_List(String vendor_Order_Id, String order_date, String payment_status, String payment_method, String transaction_id, String order_status, String grand_total, String vendor_Name) {
        Vendor_Order_Id = vendor_Order_Id;
        Order_date = order_date;
        Payment_status = payment_status;
        Payment_method = payment_method;
        Transaction_id = transaction_id;
        Order_status = order_status;
        Grand_total = grand_total;
        Vendor_Name = vendor_Name;
    }
}
