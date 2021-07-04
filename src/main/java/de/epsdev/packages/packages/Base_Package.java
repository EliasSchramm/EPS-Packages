package de.epsdev.packages.packages;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import de.epsdev.packages.encryption.AES_Key;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Base_Package {
    private String name;
    private int id;
    private JSONObject base_data;

    private HashMap<String, String> string_values = new HashMap<>();
    private final Type string_values_type = new TypeToken<HashMap<String, String>>(){}.getType();
    private HashMap<String, String[]> string_a_values = new HashMap<>();
    private final Type string_a_values_type = new TypeToken<HashMap<String, String[]>>(){}.getType();

    private HashMap<String, Integer> int_values = new HashMap<>();
    private final Type int_values_type = new TypeToken<HashMap<String, Integer>>(){}.getType();
    private HashMap<String, Integer[]> int_a_values = new HashMap<>();
    private final Type int_a_values_type = new TypeToken<HashMap<String, Integer[]>>(){}.getType();

    private HashMap<String, Float> float_values = new HashMap<>();
    private final Type float_values_type = new TypeToken<HashMap<String, Float>>(){}.getType();
    private HashMap<String, Float[]> float_a_values = new HashMap<>();
    private final Type float_a_values_type = new TypeToken<HashMap<String, Float[]>>(){}.getType();

    private HashMap<String, Double> double_values = new HashMap<>();
    private final Type double_values_type = new TypeToken<HashMap<String, Double>>(){}.getType();
    private HashMap<String, Double[]> double_a_values = new HashMap<>();
    private final Type double_a_values_type = new TypeToken<HashMap<String, Double[]>>(){}.getType();

    private HashMap<String, Long> long_values = new HashMap<>();
    private final Type long_values_type = new TypeToken<HashMap<String, Long>>(){}.getType();
    private HashMap<String, Long[]> long_a_values = new HashMap<>();
    private final Type long_a_values_type = new TypeToken<HashMap<String, Long[]>>(){}.getType();

    private HashMap<String, Boolean> boolean_values = new HashMap<>();
    private final Type boolean_values_type = new TypeToken<HashMap<String, Boolean>>(){}.getType();
    private HashMap<String, Boolean[]> boolean_a_values = new HashMap<>();
    private final Type boolean_a_values_type = new TypeToken<HashMap<String, Boolean[]>>(){}.getType();

    public Base_Package(String name){
        this.name = name;
        this.id = ThreadLocalRandom.current().nextInt(100000000, 2000000000);
    }

    public Base_Package(String base64, Socket s) {
        this.id = ThreadLocalRandom.current().nextInt(100000000, 2000000000);
        String decoded = decrypt(base64, s);

        if(decoded.equalsIgnoreCase("")) new PackageServerError("Package was empty or wasn't properly encoded.");

        JSONObject o = new JSONObject(decoded);
        this.base_data = o;

        assignValuesByJSONObject(o);
    }

    public Base_Package(Base_Package base_package){
        this.id = base_package.getID();
        this.name = base_package.getName();

        assignValuesByJSONObject(base_package.getBase_data());
    }

    public int getID(){
        return this.id;
    }

    public JSONObject getBase_data(){
        return this.base_data;
    }

    private void assignValuesByJSONObject(JSONObject o){
        this.name = o.getString("package_name");
        JSONObject data = o.getJSONObject("data");

        List<String> o_keys = new ArrayList<>();
        data.keys().forEachRemaining(o_keys::add);

        if (o_keys.contains("str")) this.string_values = new Gson().fromJson(data.getJSONObject("str").toString(), string_values_type);
        if (o_keys.contains("str_a")) this.string_a_values = new Gson().fromJson(data.getJSONObject("str_a").toString(), string_a_values_type);

        if (o_keys.contains("int")) this.int_values = new Gson().fromJson(data.getJSONObject("int").toString(), int_values_type);
        if (o_keys.contains("int_a")) this.int_a_values = new Gson().fromJson(data.getJSONObject("int_a").toString(), int_a_values_type);

        if (o_keys.contains("float")) this.float_values = new Gson().fromJson(data.getJSONObject("float").toString(), float_values_type);
        if (o_keys.contains("float_a")) this.float_a_values = new Gson().fromJson(data.getJSONObject("float_a").toString(), float_a_values_type);

        if (o_keys.contains("double")) this.double_values = new Gson().fromJson(data.getJSONObject("double").toString(), double_values_type);
        if (o_keys.contains("double_a")) this.double_a_values = new Gson().fromJson(data.getJSONObject("double_a").toString(), double_a_values_type);

        if (o_keys.contains("long")) this.long_values = new Gson().fromJson(data.getJSONObject("long").toString(), long_values_type);
        if (o_keys.contains("long_a")) this.long_a_values = new Gson().fromJson(data.getJSONObject("long_a").toString(), long_a_values_type);

        if (o_keys.contains("boolean")) this.boolean_values = new Gson().fromJson(data.getJSONObject("boolean").toString(), boolean_values_type);
        if (o_keys.contains("boolean_a")) this.boolean_a_values = new Gson().fromJson(data.getJSONObject("boolean_a").toString(), boolean_a_values_type);
    }

    public boolean add(String field_name, Object value){
        if(field_name.length() == 0) return false;


        if(value instanceof String){
            string_values.put(field_name, (String) value);
        }else if(value instanceof String[]){
            string_a_values.put(field_name, (String[]) value);
        }else if(value instanceof Integer){
            int_values.put(field_name, (int) value);
        }else if(value instanceof Integer[]){
            int_a_values.put(field_name, (Integer[]) value);
        }else if(value instanceof Float){
            float_values.put(field_name, (float) value);
        }else if(value instanceof Float[]){
            float_a_values.put(field_name, (Float[]) value);
        }else if(value instanceof Double){
            double_values.put(field_name, (double) value);
        }else if(value instanceof Double[]){
            double_a_values.put(field_name, (Double[]) value);
        }else if(value instanceof Long){
            long_values.put(field_name, (long) value);
        }else if(value instanceof Long[]){
            long_a_values.put(field_name, (Long[]) value);
        }else if(value instanceof Boolean){
            boolean_values.put(field_name, (boolean) value);
        }else if(value instanceof Boolean[]){
            boolean_a_values.put(field_name, (Boolean[]) value);
        }else return false;

        return true;
    }

    private String genJsonString(){
        JSONObject out = new JSONObject();
        out.put("package_name", this.name);

        JSONObject _out = new JSONObject();

        if(!this.string_values.isEmpty()) _out.put("str",new JSONObject(this.string_values));
        if(!this.string_a_values.isEmpty()) _out.put("str_a",new JSONObject(this.string_a_values));

        if(!this.int_values.isEmpty()) _out.put("int",new JSONObject(this.int_values));
        if(!this.int_a_values.isEmpty()) _out.put("int_a",new JSONObject(this.int_a_values));

        if(!this.float_values.isEmpty()) _out.put("float",new JSONObject(this.float_values));
        if(!this.float_a_values.isEmpty()) _out.put("float_a",new JSONObject(this.float_a_values));

        if(!this.double_values.isEmpty()) _out.put("double",new JSONObject(this.double_values));
        if(!this.double_a_values.isEmpty()) _out.put("double_a",new JSONObject(this.double_a_values));

        if(!this.long_values.isEmpty()) _out.put("long",new JSONObject(this.long_values));
        if(!this.long_a_values.isEmpty()) _out.put("long_a",new JSONObject(this.long_a_values));

        if(!this.boolean_values.isEmpty()) _out.put("boolean",new JSONObject(this.boolean_values));
        if(!this.boolean_a_values.isEmpty()) _out.put("boolean_a",new JSONObject(this.boolean_a_values));

        out.put("data", _out);

        return out.toString();
    }

    public String getName(){
        return this.name;
    }

    public String getString(String field_name){
        return this.string_values.getOrDefault(field_name, null);
    }

    public String[] getStringArray(String field_name){
        return this.string_a_values.getOrDefault(field_name, null);
    }

    public int getInteger(String field_name){
        return this.int_values.getOrDefault(field_name, null);
    }

    public Integer[] getIntegerArray(String field_name){
        return this.int_a_values.getOrDefault(field_name, null);
    }

    public float getFloat(String field_name){
        return this.float_values.getOrDefault(field_name, null);
    }

    public Float[] getFloatArray(String field_name){
        return this.float_a_values.getOrDefault(field_name, null);
    }

    public double getDouble(String field_name){
        return this.double_values.getOrDefault(field_name, null);
    }

    public Double[] getDoubleArray(String field_name){
        return this.double_a_values.getOrDefault(field_name, null);
    }

    public long getLong(String field_name){
        return this.long_values.getOrDefault(field_name, null);
    }

    public Long[] getLongArray(String field_name){
        return this.long_a_values.getOrDefault(field_name, null);
    }

    public boolean getBoolean(String field_name){
        return this.boolean_values.getOrDefault(field_name, null);
    }

    public Boolean[] getBooleanArray(String field_name){
        return this.boolean_a_values.getOrDefault(field_name, null);
    }

    private String encrypt(String str, Socket s){

        if (Package.CLIENT_KEYS.containsKey(s.toString())){
            AES_Key k = Package.CLIENT_KEYS.get(s.toString());
            return k.encrypt(str);
        }

        return str;
    }

    private String decrypt(String str, Socket s){

        if (Package.KEYS != null){
            AES_Key k = Package.CLIENT_KEYS.get(s.toString());
            return k.decrypt(str);
        }

        return str;
    }

    public void send(Socket s) throws IOException {

        OutputStream outToServer = s.getOutputStream();
        DataOutputStream out = new DataOutputStream(outToServer);
        String toBeSend = encrypt(genJsonString(), s);

        if(Package.getPackageSize(s) != 0){
            ArrayList<String> parts = new ArrayList<>();

            String temp = "";
            for (String _char : toBeSend.split("")) {
                if(temp.getBytes(StandardCharsets.UTF_8).length >= Package.getPackageSize(s)){
                    parts.add(temp);
                    temp = "";
                }
                temp += _char;
            }
            parts.add(temp);

            for (int i = 0; i < parts.size(); i++) {
                parts.set(i, (i + 1) + "|" + parts.size() + "|" + this.id + "|" + parts.get(i));
            }

            for (String part : parts) out.writeUTF(part);
        }else {
            out.writeUTF("1|1|" + this.id + "|" + toBeSend);
        }
    }
}
