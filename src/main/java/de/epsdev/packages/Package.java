package de.epsdev.packages;

import com.google.gson.Gson;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Package {
    private final String name;
    private final int id;

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

    public OnPackageReceive onPackageReceive;

    public Package(String name, OnPackageReceive onPackageReceive){
        this.name = name;
        this.onPackageReceive = onPackageReceive;
        this.id = ThreadLocalRandom.current().nextInt(100000000, 1000000000);
    }

    public Package(String base64) {
        this.id = ThreadLocalRandom.current().nextInt(100000000, 1000000000);
        byte[] _decoded = Base64.getMimeDecoder().decode(base64);
        String decoded = new String(_decoded);

        JSONObject o = new JSONObject(decoded);
        this.name = o.getString("package_name");

        JSONObject data = o.getJSONObject("data");

        this.string_values = new Gson().fromJson(data.getJSONObject("str").toString(), HashMap.class);
        this.string_a_values = new Gson().fromJson(data.getJSONObject("str_a").toString(), HashMap.class);

        this.int_values = new Gson().fromJson(data.getJSONObject("int").toString(), HashMap.class);
        this.int_a_values = new Gson().fromJson(data.getJSONObject("int_a").toString(), HashMap.class);

        this.float_values = new Gson().fromJson(data.getJSONObject("float").toString(), HashMap.class);
        this.float_a_values = new Gson().fromJson(data.getJSONObject("float_a").toString(), HashMap.class);

        this.double_values = new Gson().fromJson(data.getJSONObject("str").toString(), HashMap.class);
        this.double_a_values = new Gson().fromJson(data.getJSONObject("str_a").toString(), HashMap.class);

        this.long_values = new Gson().fromJson(data.getJSONObject("long").toString(), HashMap.class);
        this.long_a_values = new Gson().fromJson(data.getJSONObject("long").toString(), HashMap.class);

        this.boolean_values = new Gson().fromJson(data.getJSONObject("boolean").toString(), HashMap.class);
        this.boolean_a_values = new Gson().fromJson(data.getJSONObject("boolean_a").toString(), HashMap.class);
    }

    public void send(Socket s){
        try {
            OutputStream outToServer = s.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);

            out.writeUTF(Base64.getMimeEncoder().encodeToString(genJsonString().getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
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

        _out.put("str",new JSONObject(this.string_values));
        _out.put("str_a",new JSONObject(this.string_a_values));

        _out.put("int",new JSONObject(this.int_values));
        _out.put("int_a",new JSONObject(this.int_a_values));

        _out.put("float",new JSONObject(this.float_values));
        _out.put("float_a",new JSONObject(this.float_a_values));

        _out.put("double",new JSONObject(this.double_values));
        _out.put("double_a",new JSONObject(this.double_a_values));

        _out.put("long",new JSONObject(this.long_values));
        _out.put("long_a",new JSONObject(this.long_a_values));

        _out.put("boolean",new JSONObject(this.boolean_values));
        _out.put("boolean_a",new JSONObject(this.boolean_a_values));

        out.put("data", _out);

        return out.toString();
    }

    public String getName(){
        return this.name;
    }

}
