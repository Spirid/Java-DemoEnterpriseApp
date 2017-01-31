package enterprisejdbcclient;

import EnterpriseJDBCServer.DBSessionBeanRemote;
import java.util.Arrays;

public class QueryClass {
    
    boolean Ok = false;
    private String func = null;
    private int argc = -1;
    private String[] argv = null;
    private String[] result = null;
    
    public QueryClass(String query)
    {
        if (query == null) {return;}
        
        String[] preSplitQuery = query.split(";");
        
        String[] splitQuery = new String[preSplitQuery.length - 1];
        for (int i = 0; i < (preSplitQuery.length - 1); i++) {
            splitQuery[i] = preSplitQuery[i];
        }
        
        if (splitQuery.length < 1) {return;}
        
        if (!setFunction(splitQuery[0])) {return;}
        if (!setArgc(splitQuery[1])) {return;}
        if (!setArgv(splitQuery)) {return;}
        Ok = true;
    }
    
    private boolean setFunction(String funcStr)
    {
        String[] funcs = funcStr.split("=");
        if (funcs.length == 2) {
            if ("function".equals(funcs[0]))
            {
                func = funcs[1];
                return true;                
            }
        }
        return false;
    }
    
     private boolean setArgc(String argcStr)
    {
        String[] argcs = argcStr.split("=");
        if (argcs.length == 2) {
            if ("argc".equals(argcs[0]))
            {
                try {
                argc = Integer.valueOf(argcs[1]);
                } catch (NumberFormatException e)
                {
                    return false;
                }
                return true;                
            }
        }
        return false;
    }
     
     private boolean setArgv(String[] argvStr)
     {
         if (2 == argvStr.length && 0 == argc)
         {
             return true;
         } else {
             if (argvStr.length != argc + 2) {return false;}
         }
         
         argv = new String[argc];
         for (int i = 2; i < argvStr.length; ++i)
         {
             argv[i - 2] = argvStr[i];
         }      
         return true;
     }
    
     public boolean getStatusQuery()
     {
         return Ok;
     }
     
     public boolean getStatusQueryResult()
     {
         return null != result;
     }
     
     private boolean setResult(String[] result)
     {
         if (null != result && null == this.result) {
         this.result = result;
         return true;
         }
         return false;
     }
     
     public boolean setResult(String result)
     {
         if (null != result && null == this.result) {
         this.result = new String[1];
         this.result[0] = result;
         return true;
         }
         return false;
     }
     
     public String getResult()
     {
         return Arrays.toString(result);
     }
     
     public String getFunction() {
           return func;
     }
     
     public String[] getArgv()
     {
         return argv;
     }
     
     public int getArgc()
     {
         return argc;
     }
     
    synchronized public void executeQuery(DBSessionBeanRemote dBSessionBean) {

         if (null == func)
         {
             String[] s = new String[1];
             s[0] = "false";
             setResult(s);
         }
         
        switch (func) {
            case "login" :
                setResult(Boolean.toString(dBSessionBean.login(argv[0], argv[1])));
                break;
            case "getHistory" :               
                try {
                    int code = Integer.valueOf(argv[0]);
                    setResult(dBSessionBean.getHistory(code));
                } catch (NumberFormatException ex)
                {
                    setResult(dBSessionBean.getHistory(argv[0]));
                }
                break;
            case "logout" :
                setResult(Boolean.toString(dBSessionBean.logout()));
                break;
            default: 
                setResult("Error");
                break;
        }
    }
     

}
