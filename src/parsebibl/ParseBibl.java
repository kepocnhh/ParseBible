package parsebibl;

import api.API;
import ice.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ParseBibl
{
    static private String accpath = "D:\\Загрузки\\ForIce\\accounts\\account";
    static private String new_accpath = 
            //"D:\\Загрузки\\ForIce\\accounts\\account_new"
            "/home/toha/ForIce/account_new"
            ;
    static private String logpath = 
            //"D:\\ForIce\\IceServer\\log\\"
            "/home/toha/ForIce/OldLogs/"
            ;
    static private String newpath = 
            //"D:\\ForIce\\log\\"
            "/home/toha/ForIce/NewLogs/"
            ;
    static private String parsepath = 
            //"D:\\ForIce\\Parse\\log\\"
            "/home/toha/ForIce/ParseLogs/"
            ;
    static private Itg_old itg;
    static private BaseMessage copy_to_new(BM_old bmo, BaseMessage bm)
    {
        bm.SetDate(bmo.GetDate());
        bm.SetPlace(bmo.GetX(), bmo.GetY());
        if(bmo.GetVersion()!=null)
        {
            bm.SetVersion(bmo.GetVersion());
        }
        return bm;
    }
    static private ping copy_to_new(ping_old po)
    {
        ping p = new ping(po.GetPing());
        p = (ping) copy_to_new((BM_old)po, (BaseMessage)p);
        return p;
    }
    static private DataForRecord copy_to_new(DFR_old dfro)
    {
        DataForRecord dfr = null;
        try{
            dfr = new DataForRecord(new Strings("/home/toha/ForIce/config"));
        } catch (IOException ex) {
        }
        //DataForRecord dfr = null;
        dfr = (DataForRecord) copy_to_new((BM_old)dfro, (BaseMessage)dfr);
        if(dfro.nameshop!=null)
        {
            //dfr.nameshop=dfro.nameshop;
        }
        dfr.matrix=dfro.matrix;
        dfr.setCash(dfro.getCash());
        int i=0;
        if(dfro.getTypeEvent()==0)
        {
            i=0;
        }
        if(dfro.getTypeEvent()==2)
        {
            i=1;
        }
        if(dfro.getTypeEvent()==3)
        {
            i=2;
        }
        if(dfro.getTypeEvent()==1)
        {
            i=3;
        }
        dfr.setTypeEvent(DataForRecord.TypeEvent.values()[i]);
        return dfr;
    }
    static private DataCass copy_to_new(DC_old dco)
    {
        DataCass dc = new DataCass(dco.Cash,dco.Fam,DataCass.TypeEvent.values()[dco.TE]);
        dc = (DataCass) copy_to_new((BM_old)dco, (BaseMessage)dc);
        return dc;
    }
    static private BM_old copy_from_old(BM_old bmo, BaseMessage bm)
    {
        bmo.SetDate(bm.GetDate());
        bmo.SetPlace(bm.GetX(), bm.GetY());
        if(bm.GetVersion()!=null)
        {
            bmo.SetVersion(bm.GetVersion());
        }
        return bmo;
    }
    static private ping_old copy_from_old(ping p)
    {
        ping_old po = new ping_old(p.GetPing());
        if(p.GetPing().equals("open"))
        {
            itg.date_open = p.GetDate();
        }
        po = (ping_old) copy_from_old((BM_old)po, (BaseMessage)p);
        return po;
    }
    static private DFR_old copy_from_old(DataForRecord dfr)
    {
        DFR_old dfro = new DFR_old();
        dfro = (DFR_old) copy_from_old((BM_old)dfro, (BaseMessage)dfr);
        /*
                if(dfr.nameshop!=null)
                {
                    itg.nameshop=dfr.nameshop;
                }
                */
                if(dfr.getTypeEvent() ==DataForRecord.TypeEvent.close)
                {
                    itg.date_close = dfr.GetDate();
                }
                
        dfro.matrix=dfr.matrix;
        dfro.setCash(dfr.getCash());
        dfro.setTypeEvent(dfr.getTypeEvent().ordinal());
        return dfro;
    }
    static private DC_old copy_from_old(DataCass dc)
    {
        DC_old dco = new DC_old();
        dco = (DC_old) copy_from_old((BM_old)dco, (BaseMessage)dc);
        dco.TE=(dc.getTypeEvent().ordinal());
        return dco;
    }
    static private List<BM_old> ParseBibl_old(String path_from,String path_to,String file) throws FileNotFoundException, IOException, ClassNotFoundException
    {
        FileInputStream fis = new FileInputStream(path_from+file);
        ObjectInputStream read = new ObjectInputStream(fis);
            List<BaseMessage> loglist = (List) read.readObject();
        fis.close();
        read.close();
        itg = new Itg_old();
                    List<BM_old> list_old = new ArrayList<>();
                    for(int i=0;i<loglist.size();i++)
                    {
                        Class c = loglist.get(i).getClass();
                        if (c == ping.class)
                        {
                            list_old.add((BM_old)copy_from_old((ping)loglist.get(i)));
                        }
                        if (c == DataForRecord.class)
                        {
                            list_old.add((BM_old)copy_from_old((DataForRecord)loglist.get(i)));
                        }
                        if (c == DataCass.class)
                        {
                            list_old.add((BM_old)copy_from_old((DataCass)loglist.get(i)));
                        }
                    }
                            list_old.add((BM_old)itg);
        File myPath = new File(path_to);
        myPath.mkdir();
        myPath.mkdirs();
                FileOutputStream fos = new FileOutputStream(path_to+file);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                    oos.writeObject(list_old);
                fos.close();
                oos.close();
        return list_old;
    }
    
    static private List<BaseMessage> ParseBibl_new(String path_from,String path_to,String file,List<user> ul) throws FileNotFoundException, IOException, ClassNotFoundException
    {
        FileInputStream fis = new FileInputStream(path_from+file);
        ObjectInputStream read = new ObjectInputStream(fis);
                    List<BM_old> list_old = (List) read.readObject();
        fis.close();
        read.close();
                    List<BaseMessage> loglist = new ArrayList<>();
                    user newuser = null;
                    for(int i=0;i<ul.size();i++)
                    {
                        if(file.lastIndexOf(ul.get(i).GetSurname()) != -1)
                        {
                            newuser = ul.get(i);
                            break;
                        }
                    }
                   Itog newitog = new Itog(newuser.GetMail());
                   boolean opn = false;
                   boolean cls = false;
                    for(int i=0;i<list_old.size();i++)
                    {
                        Class c = list_old.get(i).getClass();
                        if (c == Itg_old.class)
                        {
                            Itg_old olditog = (Itg_old)list_old.get(i);
                            newitog.date_open = olditog.date_open;
                            newitog.date_close = olditog.date_close;
                            newitog.nameshop = olditog.nameshop;
                        }
                        if (c == ping_old.class)
                        {
                            loglist.add((BaseMessage)copy_to_new((ping_old)list_old.get(i)));
                        }
                        if (c == DFR_old.class)
                        {
                            DFR_old tmp = (DFR_old)list_old.get(i);
                            if(tmp.getTypeEvent() == 0)
                            {
                                opn = true;
                            }
                            if(tmp.getTypeEvent() == 1)
                            {
                                cls = true;
                            }
                            loglist.add((BaseMessage)copy_to_new((DFR_old)list_old.get(i)));
                        }
                        if (c == DC_old.class)
                        {
                            loglist.add((BaseMessage)copy_to_new((DC_old)list_old.get(i)));
                        }
                    }
                    if(opn&&cls)
                    {
                        
                    }
                    else
                    {
                        System.out.println(file + "\tERROR!!!");
                        return loglist;
                    }
                    newitog.day_otw = API.weektoString(newitog.date_open.getDay());
                    newitog = API.Calculate_Itog(newitog, newuser, loglist);//пересчитываем объект итогов
                            loglist.add((BaseMessage)newitog);
        File myPath = new File(path_to);
        myPath.mkdir();
        myPath.mkdirs();
                FileOutputStream fos = new FileOutputStream(path_to+file+ ".ice");
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                    oos.writeObject(loglist);
                fos.close();
                oos.close();
        return loglist;
    }
    
    private static void Parse_new(Date bgn, Date end)
    {
        List<String> bmlist=null;
        try
        {
            bmlist = API.Get_String_List(new_accpath); //список с данными пользователей
        }
        catch (IOException ex)
        {}
        catch (ClassNotFoundException ex)
        {}
        List<user> userlist = new ArrayList();
            for (String bmlist1 : bmlist)
            {
                DateFormat df2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                Date dt=null;
                try
                {
                    dt = df2.parse(bmlist1.split("\t")[16]);
                }
                catch (ParseException ex)
                {
                }
                
                userlist.add(new user(
                        bmlist1.split("\t")[0],//name
                        bmlist1.split("\t")[1],//sur
                        bmlist1.split("\t")[2],//patr
                        bmlist1.split("\t")[3],//phone
                        bmlist1.split("\t")[4],//mail
                        bmlist1.split("\t")[5],//pass
                        bmlist1.split("\t")[6],//b
                        Boolean.parseBoolean(bmlist1.split("\t")[7]),
                        dt,//date
                        Double.parseDouble(bmlist1.split("\t")[8]),
                        Double.parseDouble(bmlist1.split("\t")[9]),
                        Double.parseDouble(bmlist1.split("\t")[10]),
                        Double.parseDouble(bmlist1.split("\t")[11]),
                        Double.parseDouble(bmlist1.split("\t")[12]),
                        Double.parseDouble(bmlist1.split("\t")[13]),
                        Double.parseDouble(bmlist1.split("\t")[14]),
                        Double.parseDouble(bmlist1.split("\t")[15])
                ));
                
            }
            while(bgn.before(end))
            {
                String path = (bgn.getYear()+1900)+"/"+(bgn.getMonth()+1)+"/"+bgn.getDate()+"/";
                String[] files = new File(parsepath+path).list();
                for(int i=0; i<files.length;i++)
                {
                    if(files[i].equals("photo")||files[i].equals("pdf"))
                    {
                        continue;
                    }
                    if(files[i].equals("supers"))
                    {
                        String path2= path + "supers"+"/";
                        String[] files_supers = new File(parsepath+path2).list();
                        for(int j=0; j<files_supers.length;j++)
                        {
                            String superpth= path2+ files_supers[j]+"/";
                            String[] _super = new File(parsepath+superpth).list();
                            for(int k=0; k<_super.length;k++)
                            {
                                if(_super[k].equals("photo")||_super[k].equals("pdf"))
                                {
                                    continue;
                                }
                                
                                try {
                                    ParseBibl_new(parsepath+superpth,newpath+superpth,_super[k],userlist);
                                } catch (IOException ex) {
                                    System.out.println(_super[k]+" "+ex.getMessage());
                                    continue;
                                } catch (ClassNotFoundException ex) {
                                    System.out.println(_super[k]+" "+ex.getMessage());
                                    continue;
                                }
                                
                                   System.out.println(superpth+_super[k]);
                            }
                        }
                        continue;
                    }
                    
                    try {
                        ParseBibl_new(parsepath+path,newpath+path,files[i],userlist);
                    } catch (IOException ex) {
                        System.out.println(files[i]+" "+ex.getMessage());
                        continue;
                    } catch (ClassNotFoundException ex) {
                        System.out.println(files[i]+" "+ex.getMessage());
                        continue;
                    }
                    
                    System.out.println(path+files[i]);
                }
                bgn.setDate(bgn.getDate()+1);
            }
        
    }
    private static void Parse_old(Date bgn, Date end)
    {
            while(bgn.before(end))
            {
                String path = (bgn.getYear()+1900)+"/"+(bgn.getMonth()+1)+"/"+bgn.getDate()+"/";
                String[] files = new File(logpath+path).list();
                for(int i=0; i<files.length;i++)
                {
                    if(files[i].equals("photo")||files[i].equals("pdf"))
                    {
                        continue;
                    }
                    if(files[i].equals("supers"))
                    {
                        String path2 = path+"supers"+"/";
                        String[] files_supers = new File(logpath+path2).list();
                        for(int j=0; j<files_supers.length;j++)
                        {
                            String superpth= path2+ files_supers[j]+"/";
                            String[] _super = new File(logpath+superpth).list();
                            for(int k=0; k<_super.length;k++)
                            {
                                if(_super[k].equals("photo")||_super[k].equals("pdf"))
                                {
                                    continue;
                                }
                                try {
                                    ParseBibl_old(logpath+superpth,parsepath+superpth,_super[k]);
                                } catch (IOException ex) {
                                    System.out.println(_super[k]+" "+ex.getMessage());
                                    continue;
                                } catch (ClassNotFoundException ex) {
                                    System.out.println(_super[k]+" "+ex.getMessage());
                                    continue;
                                }
                                   System.out.println(_super[k]);
                            }
                        }
                        continue;
                    }
                    try {
                        List<BM_old> loglist = ParseBibl_old(logpath+path,parsepath+path,files[i]);
                    } catch (IOException ex) {
                        System.out.println(files[i]+" "+ex.getMessage());
                        continue;
                    } catch (ClassNotFoundException ex) {
                        System.out.println(files[i]+" "+ex.getMessage());
                        continue;
                    }
                    System.out.println(path+files[i]);
                }
                bgn.setDate(bgn.getDate()+1);
            }
    }
    public static void main(String[] args)
    {
            Date bgn = new Date();
            bgn.setYear(114);
            bgn.setMonth(6);
            bgn.setDate(1);
            Date end = new Date();
            end.setYear(114);
            end.setMonth(6);
            end.setDate(30);
            //
            //Parse_old(bgn,end);
            Parse_new(bgn,end);
    }
}