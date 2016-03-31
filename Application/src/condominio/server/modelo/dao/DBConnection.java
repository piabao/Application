package condominio.server.modelo.dao;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.CadastroEntity;
import model.TelefoneEntity;

import org.hsqldb.types.Types;

public class DBConnection {

    private static Connection con;
    
    public static Connection getConnection(){
        try {
        	
        	 Class.forName("org.hsqldb.jdbcDriver");
             con = DriverManager.getConnection("jdbc:hsqldb:file:db/condominio", "sa", "");
        	
            //-----------------MYSQL----------------------------------------------------
//        	con = DriverManager.getConnection("jdbc:mysql://localhost:3306/team3?" +
//                     "user=root&password=IT1639");
        	
        }catch(Exception e){
            System.out.println("SQL Error: "+e);
            e.printStackTrace();
        }
    return con;
    }
    
    
    public static void shutdown(){
        Statement st;
		try {
			st = con.createStatement();
	        // db writes out to files and performs clean shuts down
	        // otherwise there will be an unclean shutdown
	        // when program ends
	        st.execute("SHUTDOWN");
	        con.close();    // if there are no other open connection
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    public static void close(ResultSet rs, Statement stm){
        try {
        	if(rs != null){
        		rs.close();        		
        	}
            stm.close();
        } catch (SQLException ex) {
            System.out.println("SQL Error: "+ex);
            ex.printStackTrace();
        }
    }
    
    @SuppressWarnings("unchecked")
	public static <T> T executeSQL(String sql){
        try {
        	if(con == null){
        		getConnection();        		
        	}
            Statement stm= con.createStatement();
            stm.execute(sql, Statement.RETURN_GENERATED_KEYS);
            ResultSet keyset = stm.getGeneratedKeys();
            Integer key = 0;
            if ( keyset.next() ) {
                // Retrieve the auto generated key(s).
                key = keyset.getInt(1);
            }
            DBConnection.close(keyset,stm);
            return  (T) key;
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    @SuppressWarnings("unused")
	private Map<String, Map<Field, String>> getMethod(Field[] declaredFields, String type){
        Map<String, Map<Field, String>> fields = new TreeMap<String, Map<Field, String>>(String.CASE_INSENSITIVE_ORDER);
        for (Field field: declaredFields) {
                String methodName = type + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
                HashMap<Field, String> hashMap = new HashMap<Field, String>();
                hashMap.put(field, methodName);
                fields.put(field.getName(), hashMap);
        }
        return fields;
    }
    
    @SuppressWarnings("unchecked")
	public static <T> List<T> executeSelect(String QUERY, Object obj){
        List<Object> data = new ArrayList<>();
        try {
        	if(con == null){
        		getConnection();        		
        	}
            Statement stm= con.createStatement();
            ResultSet rs = stm.executeQuery(QUERY);

            data = extractData(rs, obj);
            DBConnection.close(rs,stm);
            
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            return new ArrayList<>();
        }
        return (List<T>) data;
    }
    
    private static List<Object> extractData(ResultSet rs, Object objct) throws SQLException {
        List<Object> retorno = new ArrayList<Object>();

        Field[] declaredFields = objct.getClass().getDeclaredFields();
        Map<String, Map<Field, String>> fields = new TreeMap<String, Map<Field, String>>(String.CASE_INSENSITIVE_ORDER);
        for (Field field: declaredFields) {
                String methodName = "set" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
                HashMap<Field, String> hashMap = new HashMap<Field, String>();
                hashMap.put(field, methodName);
                fields.put(field.getName(), hashMap);
        }
        while (rs.next()) {	
            Object obj = null;
            try {
                    obj = objct.getClass().newInstance();
            } catch (InstantiationException | IllegalAccessException e1) {
                    e1.printStackTrace();
            }
            ResultSetMetaData meta = rs.getMetaData();
            mountFields(rs, fields, obj, meta);
            retorno.add(obj);
        }
        return retorno;
    }
    
    private static void mountFields(ResultSet rs, Map<String, Map<Field, String>> fields, Object obj, ResultSetMetaData meta) throws SQLException {
        for (int i = 1, n = meta.getColumnCount() + 1; i < n; i++) {
            String methodName = null;
            if (rs.getObject(i) == null) {
                    continue;
            }
            try {
                Map<Field, String> map = fields.get(meta.getColumnName(i));
                if (map == null) {
                    System.err.println(meta.getColumnName(i) + " not mapped " + obj.getClass());
                    continue;
                }
                methodName = map.values().iterator().next();
                Method method = obj.getClass().getDeclaredMethod(methodName, map.keySet().iterator().next().getType());
                if(meta.getColumnType(i) == Types.SQL_INTEGER && method.getParameterTypes()[0] == Long.class){
                    method.invoke(obj, Long.parseLong(rs.getObject(i).toString()));
                }else{
                    method.invoke(obj, rs.getObject(i));                    
                }
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
                        | NoSuchMethodException | SecurityException e) {
                System.out.println(meta.getColumnType(i));
                System.out.println(meta.getColumnName(i));
                System.out.println(rs.getObject(i));
                e.printStackTrace();
            }
        }
    }
    
    public static Long insertHaitiano(CadastroEntity hay){
    	String sqlHay;
    	if(hay.getData() == null){
    		sqlHay = "INSERT INTO HAITIANO (NOME,ENDERECO) "
        			+ "VALUES ('"+hay.getNome()+"','"+hay.getEndereco()+"')";
    	}else{
    		sqlHay = "INSERT INTO HAITIANO (NOME,ENDERECO,DATA) "
    				+ "VALUES ('"+hay.getNome()+"','"+hay.getEndereco()+"','"+hay.getData()+"')";
    	}
    	Integer id = executeSQL(sqlHay);
    	for (TelefoneEntity tel : hay.getTelefone()) {			
	    	String sql = "INSERT INTO TELEFONE (HAITIANO, TELEFONE, WHATS) "
	    			+ "VALUES ('"+id+"','"+tel.getTelefone()+"','"+tel.isWhats()+"')";
	    	executeSQL(sql);
    	}
    	return id.longValue();
    }

	public static Long deleteHaitiano(Integer id) {
		List<TelefoneEntity> telefones = executeSelect("SELECT * from TELEFONE where haitiano = "+ id, new TelefoneEntity());
		for (TelefoneEntity tel : telefones) {
			executeSQL("DELETE FROM TELEFONE WHERE id = "+tel.getId());
		}
		Integer idHai = executeSQL("DELETE FROM HAITIANO WHERE id = "+id);
		return idHai.longValue();
	}

	public static Long deleteTelefone(Integer id) {
		Integer idHai = executeSQL("DELETE FROM TELEFONE WHERE id = "+id);
		return idHai.longValue();
	}

	public static Long updateHaitiano(CadastroEntity item) {
		String sqlHay;
		if(item.getData() == null){
    		sqlHay = "UPDATE HAITIANO SET NOME='"+item.getNome()+"', ENDERECO='"+item.getEndereco()+"' WHERE id="+item.getId();
    	}else{
    		sqlHay = "UPDATE HAITIANO SET NOME='"+item.getNome()+"', ENDERECO='"+item.getEndereco()+"', "
    				+ "DATA ='"+item.getData()+"' WHERE id="+item.getId();
    	}
		Integer id = executeSQL(sqlHay);
		
		executeSQL("DELETE FROM TELEFONE WHERE haitiano= "+item.getId());
		for (TelefoneEntity tel : item.getTelefone()) {
			String sql = "INSERT INTO TELEFONE (HAITIANO, TELEFONE, WHATS) "
	    			+ "VALUES ('"+item.getId()+"','"+tel.getTelefone()+"','"+tel.isWhats()+"')";
	    	executeSQL(sql);
		}
		return id.longValue();
	}
}
