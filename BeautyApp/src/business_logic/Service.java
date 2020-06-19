package business_logic;

public class Service {
    public enum ServiceType {
        HAIRDRESSING, MANICURE;
        public static String serviceTypeToString(ServiceType st){
            switch (st){
                case HAIRDRESSING:
                    return "HAIRDRESSING";
                case MANICURE:
                    return "MANICURE";
                default:
                    return null;
            }
        }

        public static ServiceType stringToServiceType(String st){
            switch (st){
                case "HAIRDRESSING":
                    return HAIRDRESSING;
                case "MANICURE":
                    return MANICURE;
                default:
                    return null;
            }
        }
    }

    private Integer id;
    private String name;
    private ServiceType type;
    private Integer length;

    public Service(String name, ServiceType type, Integer length){
        this.name = name;
        this.type = type;
        this.length = length;
    }

    public Service(Integer id, String name, ServiceType type, Integer length){
        this.id = id;
        this.name = name;
        this.type = type;
        this.length = length;
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
    public ServiceType getType(){
        return type;
    }
    public Integer getLength(){
        return length;
    }

    @Override
    public String toString() {
        return this.name;
    }

}
