package android.example.foodapp.Model;

public class Order {
    private String orderID,quantity,price,total,orderOnDate, orderReceivedDate, payment, status,pid,user,address;

    public Order(String orderID,String pid, String quantity, String price, String total, String orderOnDate, String orderReceivedDate, String payment, String status, String user, String address) {
        this.orderID = orderID;
        this.quantity = quantity;
        this.price = price;
        this.total = total;
        this.orderOnDate = orderOnDate;
        this.orderReceivedDate = orderReceivedDate;
        this.payment = payment;
        this.status = status;
        this.pid = pid;
        this.user = user;
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Order() {
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getOrderOnDate() {
        return orderOnDate;
    }

    public void setOrderOnDate(String orderOnDate) {
        this.orderOnDate = orderOnDate;
    }

    public String getOrderReceivedDate() {
        return orderReceivedDate;
    }

    public void setOrderReceivedDate(String orderReceivedDate) {
        this.orderReceivedDate = orderReceivedDate;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }
}