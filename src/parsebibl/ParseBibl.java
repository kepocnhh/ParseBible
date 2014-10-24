package parsebibl;

import ice.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ParseBibl
{
    static private String accpath = "D:\\Загрузки\\ForIce\\accounts\\account";
    static private String new_accpath = "D:\\Загрузки\\ForIce\\accounts\\account_new";
    static private String logpath = "D:\\ForIce\\IceServer\\log\\";
    static private String newpath = "D:\\ForIce\\log\\";
    static private String parsepath = "D:\\ForIce\\Parse\\log\\";
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
        //DataForRecord dfr = new DataForRecord();
        DataForRecord dfr = null;
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
                    dfro.nameshop=dfr.nameshop;
                }
                */
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
        /*
        if(dc.getTypeEvent()==DataCass.TypeEvent.cass)
        {
            dco.Cash=dc.getCassCash();
            dco.Fam="";
        }
        if(dc.getTypeEvent()==DataCass.TypeEvent.inkasator)
        {
            dco.Cash=dc.getInkCash();
            dco.Fam=dc.getInkFam();
        }
        if(dc.getTypeEvent()==DataCass.TypeEvent.promoter)
        {
            dco.Cash=dc.getProCash();
            dco.Fam="";
        }
        */
        return dco;
    }
    static private List<BM_old> ParseBibl_old(String path_from,String path_to,String file) throws FileNotFoundException, IOException, ClassNotFoundException
    {
        FileInputStream fis = new FileInputStream(path_from+file);
        ObjectInputStream read = new ObjectInputStream(fis);
            List<BaseMessage> loglist = (List) read.readObject();
        fis.close();
        read.close();
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
    static private List<BaseMessage> ParseBibl_new(String path_from,String path_to,String file) throws FileNotFoundException, IOException, ClassNotFoundException
    {
        FileInputStream fis = new FileInputStream(path_from+file);
        ObjectInputStream read = new ObjectInputStream(fis);
                    List<BM_old> list_old = (List) read.readObject();
        fis.close();
        read.close();
                    List<BaseMessage> loglist = new ArrayList<>();
                    for(int i=0;i<list_old.size();i++)
                    {
                        Class c = list_old.get(i).getClass();
                        if (c == ping_old.class)
                        {
                            loglist.add((BaseMessage)copy_to_new((ping_old)list_old.get(i)));
                        }
                        if (c == DFR_old.class)
                        {
                            loglist.add((BaseMessage)copy_to_new((DFR_old)list_old.get(i)));
                        }
                        if (c == DC_old.class)
                        {
                            loglist.add((BaseMessage)copy_to_new((DC_old)list_old.get(i)));
                        }
                    }
        File myPath = new File(path_to);
        myPath.mkdir();
        myPath.mkdirs();
                FileOutputStream fos = new FileOutputStream(path_to+file);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                    oos.writeObject(loglist);
                fos.close();
                oos.close();
        return loglist;
    }
    public static void main(String[] args)
    {
        try
        {
            List<BM_old> loglist = ParseBibl_old(logpath+
                    "2014\\7\\23\\"
                    ,parsepath+
                    "2014\\7\\23\\"
                    ,
                    "Благонравова 23.7.2014"
                    );
            if (loglist != null)
            {
                for (BM_old bm : loglist)
                {
                    System.out.println(bm.toString());
                }
            }
        }
        catch (Exception ex)
        {
            System.out.println(ex.toString());
        }
    }
}