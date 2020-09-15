package android.example.foodapp.Model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Users {
    private String username,password,phone,city,pin,address;
    public Users() {
        this.username = "default";
        this.password = "default";
    }

    public Users(String username, String password, String phone, String city, String pin, String address) {
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.city = city;
        this.pin = pin;
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("username", username);
        result.put("password", password);
        result.put("city", city);
        result.put("pin", pin);
        result.put("phone", phone);
        return result;
    }
}
