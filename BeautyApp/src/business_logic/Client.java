package business_logic;

public class Client {
    private Integer id;
    private String name;
    private String phone;

    public Client(Integer id, String name, String phone){
        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    public Client(String name, String phone){
        this.name = name;
        this.phone = phone;
    }

    public Integer getId(){
        return id;
    }

    public void setId(Integer id){
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public String getPhone(){
        return phone;
    }

    @Override
    public String toString() {
        return   this.name + " (телефон: " + this.phone + ")";
    }
}
