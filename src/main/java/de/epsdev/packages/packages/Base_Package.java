package de.epsdev.packages.packages;

import com.google.gson.Gson;
import de.epsdev.packages.encryption.AES_Key;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Base_Package {
    private String name;
    private int id;
    private JSONObject base_data;

    private HashMap<String, String> string_values = new HashMap<>();
    private HashMap<String, String[]> string_a_values = new HashMap<>();

    private HashMap<String, Integer> int_values = new HashMap<>();
    private HashMap<String, int[]> int_a_values = new HashMap<>();

    private HashMap<String, Float> float_values = new HashMap<>();
    private HashMap<String, float[]> float_a_values = new HashMap<>();

    private HashMap<String, Double> double_values = new HashMap<>();
    private HashMap<String, double[]> double_a_values = new HashMap<>();

    private HashMap<String, Long> long_values = new HashMap<>();
    private HashMap<String, long[]> long_a_values = new HashMap<>();

    private HashMap<String, Boolean> boolean_values = new HashMap<>();
    private HashMap<String, boolean[]> boolean_a_values = new HashMap<>();

    public Base_Package(String name){
        this.name = name;
        this.id = ThreadLocalRandom.current().nextInt(100000000, 1000000000);
    }

    public Base_Package(String base64, Socket s) {
        this.id = ThreadLocalRandom.current().nextInt(100000000, 1000000000);
        String decoded = decrypt(base64, s);

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

        if (o_keys.contains("str")) this.string_values = new Gson().fromJson(data.getJSONObject("str").toString(), HashMap.class);
        if (o_keys.contains("str_a")) this.string_a_values = new Gson().fromJson(data.getJSONObject("str_a").toString(), HashMap.class);

        if (o_keys.contains("int")) this.int_values = new Gson().fromJson(data.getJSONObject("int").toString(), HashMap.class);
        if (o_keys.contains("int_a")) this.int_a_values = new Gson().fromJson(data.getJSONObject("int_a").toString(), HashMap.class);

        if (o_keys.contains("float")) this.float_values = new Gson().fromJson(data.getJSONObject("float").toString(), HashMap.class);
        if (o_keys.contains("float_a")) this.float_a_values = new Gson().fromJson(data.getJSONObject("float_a").toString(), HashMap.class);

        if (o_keys.contains("double")) this.double_values = new Gson().fromJson(data.getJSONObject("double").toString(), HashMap.class);
        if (o_keys.contains("double_a")) this.double_a_values = new Gson().fromJson(data.getJSONObject("double_a").toString(), HashMap.class);

        if (o_keys.contains("long")) this.long_values = new Gson().fromJson(data.getJSONObject("long").toString(), HashMap.class);
        if (o_keys.contains("long_a")) this.long_a_values = new Gson().fromJson(data.getJSONObject("long_a").toString(), HashMap.class);

        if (o_keys.contains("boolean")) this.boolean_values = new Gson().fromJson(data.getJSONObject("boolean").toString(), HashMap.class);
        if (o_keys.contains("boolean_a")) this.boolean_a_values = new Gson().fromJson(data.getJSONObject("boolean_a").toString(), HashMap.class);
    }

    public boolean add(String field_name, Object value){
        if(field_name.length() == 0) return false;

        if(value instanceof String){
            string_values.put(field_name, (String) value);
        }else if(value instanceof String[]){
            string_a_values.put(field_name, (String[]) value);
        }else if(value instanceof Integer){
            int_values.put(field_name, (int) value);
        }else if(value instanceof int[]){
            int_a_values.put(field_name, (int[]) value);
        }else if(value instanceof Float){
            float_values.put(field_name, (float) value);
        }else if(value instanceof float[]){
            float_a_values.put(field_name, (float[]) value);
        }else if(value instanceof Double){
            double_values.put(field_name, (double) value);
        }else if(value instanceof double[]){
            double_a_values.put(field_name, (double[]) value);
        }else if(value instanceof Long){
            long_values.put(field_name, (long) value);
        }else if(value instanceof long[]){
            long_a_values.put(field_name, (long[]) value);
        }else if(value instanceof Boolean){
            boolean_values.put(field_name, (boolean) value);
        }else if(value instanceof boolean[]){
            boolean_a_values.put(field_name, (boolean[]) value);
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

    public int[] getIntegerArray(String field_name){
        return this.int_a_values.getOrDefault(field_name, null);
    }

    public float getFloat(String field_name){
        return this.float_values.getOrDefault(field_name, null);
    }

    public float[] getFloatArray(String field_name){
        return this.float_a_values.getOrDefault(field_name, null);
    }

    public double getDouble(String field_name){
        return this.double_values.getOrDefault(field_name, null);
    }

    public double[] getDoubleArray(String field_name){
        return this.double_a_values.getOrDefault(field_name, null);
    }

    public long getLong(String field_name){
        return this.long_values.getOrDefault(field_name, null);
    }

    public long[] getLongArray(String field_name){
        return this.long_a_values.getOrDefault(field_name, null);
    }

    public boolean getBoolean(String field_name){
        return this.boolean_values.getOrDefault(field_name, null);
    }

    public boolean[] getBooleanArray(String field_name){
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

    public void send(Socket s){
        try {
            OutputStream outToServer = s.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);

            out.writeUTF(encrypt(genJsonString(), s));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
