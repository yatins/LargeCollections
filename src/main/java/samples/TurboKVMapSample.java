package samples;

import java.io.File;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.axiomine.largecollections.util.TurboKVMap;
import com.axiomine.largecollections.util.KryoKVMap;
import com.axiomine.largecollections.util.LargeCollection;
import com.axiomine.largecollections.utilities.FileSerDeUtils;
import com.google.common.base.Function;
import com.google.common.base.Throwables;

public class TurboKVMapSample {
    public static Function<Integer,byte[]> KSERIALIZER = new com.axiomine.largecollections.serdes.IntegerSerDes.SerFunction();
    public static Function<Integer,byte[]>  VSERIALIZER = new com.axiomine.largecollections.serdes.IntegerSerDes.SerFunction();
    public static Function<byte[],Integer>  KDESERIALIZER = new com.axiomine.largecollections.serdes.IntegerSerDes.DeSerFunction();
    public static Function<byte[],Integer>  VDESERIALIZER = new com.axiomine.largecollections.serdes.IntegerSerDes.DeSerFunction();

    /*
     * Utilize KVMap when you have Kryo Serializers for both K and V
     */
    public static void main(String[] args) {
        createKVMap();
        System.out.println("-----");
        createKVMap("KVMAP1");
        System.out.println("-----");
        createKVMap("c:/tmp","KVMAP2");
        System.out.println("-----");
        createKVMap("c:/tmp","KVMAP3",50);
        System.out.println("-----");
        createKVMap("c:/tmp","KVMAP4",50,1000);
        System.out.println("-----");
        createKVMapOveridePathAndNameWhileDSer("c:/tmp","KVMAP",50,1000);
    }
    
    public static void printMapCharacteristics(LargeCollection m){
        System.out.println("DB Path="+m.getDBPath());
        System.out.println("DB Name="+m.getDBName());
        System.out.println("Cache Size="+m.getCacheSize() + "MB");
        System.out.println("Bloomfilter Size="+m.getBloomFilterSize());
    }
    
    
    public static void workOnKVMap(TurboKVMap<Integer,Integer> map){
        try {
            for (int i = 0; i < 10; i++) {
                int r = map.put(i, i);
            }
            System.out.println("Size of map="+map.size());
            System.out.println("Value for key 0="+map.get(0));;
            System.out.println("Now remove key 0");
            int i = map.remove(0);
            System.out.println("Value for key 0(just removed)="+i);
            System.out.println("Size of map="+map.size());
            Integer nullI = map.remove(0);
            System.out.println("Re- remove key 0");
            System.out.println("Value for key 0="+nullI);
            
            System.out.println("Contains key 0="+map.containsKey(0));
            System.out.println("Contains key 1="+map.containsKey(1));
            
            System.out.println("Now closing");
            map.close();
            boolean b = false;
            try{
                map.put(0,0);    
            }
            catch(Exception ex){
                System.out.println("Exception because acces after close");
                b=true;
            }
            map.open();
            System.out.println("Open again");
            map.put(0,0);    
            System.out.println("Now put worked. Size of map should be 10. Size of the map ="+map.size());
            
            System.out.println("Now Serialize the Map");
            File serFile = new File("c:/tmp/x.ser");
            FileSerDeUtils.serializeToFile(map,serFile);            
            System.out.println("Now De-Serialize the Map");
            map = (TurboKVMap<Integer,Integer>) FileSerDeUtils.deserializeFromFile(serFile);
            System.out.println("After De-Serialization Size of map should be 10. Size of the map ="+map.size());
            printMapCharacteristics(map);
            System.out.println("Now calling map.clear()");
            map.clear();
            System.out.println("After clear map size should be 0 and ="+map.size());
            map.put(0,0);    
            System.out.println("Just added a record and map size ="+map.size());
            System.out.println("Finally Destroying");
            map.destroy();
            
            System.out.println("Cleanup serialized file");
            FileUtils.deleteQuietly(serFile);
            
            
        } catch (Exception ex) {            
            throw Throwables.propagate(ex);
        }
        
    }
    public static void createKVMap(){        
        TurboKVMap<Integer,Integer> map = new TurboKVMap<Integer,Integer>(KSERIALIZER,VSERIALIZER,KDESERIALIZER,VDESERIALIZER);
        printMapCharacteristics(map);
        workOnKVMap(map);
    }

    public static void createKVMap(String dbName){
        TurboKVMap<Integer,Integer> map = new TurboKVMap<Integer,Integer>(dbName,KSERIALIZER,VSERIALIZER,KDESERIALIZER,VDESERIALIZER);
        printMapCharacteristics(map);
        workOnKVMap(map);
    }

    public static void createKVMap(String dbPath,String dbName){
        TurboKVMap<Integer,Integer> map = new TurboKVMap<Integer,Integer>(dbPath,dbName,KSERIALIZER,VSERIALIZER,KDESERIALIZER,VDESERIALIZER);
        printMapCharacteristics(map);
        workOnKVMap(map);
    }

    public static void createKVMap(String dbPath,String dbName,int cacheSize){
        TurboKVMap<Integer,Integer> map = new TurboKVMap<Integer,Integer>(dbPath,dbName,cacheSize,KSERIALIZER,VSERIALIZER,KDESERIALIZER,VDESERIALIZER);
        printMapCharacteristics(map);
        workOnKVMap(map);
    }

    public static void createKVMap(String dbPath,String dbName,int cacheSize,int bloomfilterSize){
        TurboKVMap<Integer,Integer> map = new TurboKVMap<Integer,Integer>(dbPath,dbName,cacheSize,bloomfilterSize,KSERIALIZER,VSERIALIZER,KDESERIALIZER,VDESERIALIZER);
        printMapCharacteristics(map);
        workOnKVMap(map);
    }

    public static void createKVMapOveridePathAndNameWhileDSer(String dbPath,String dbName,int cacheSize,int bloomfilterSize){
        System.setProperty(LargeCollection.OVERRIDE_DB_PATH, dbPath);        
        System.setProperty(LargeCollection.OVERRIDE_DB_NAME, dbName);
        TurboKVMap<Integer,Integer> map = new TurboKVMap<Integer,Integer>(dbPath,dbName,cacheSize,bloomfilterSize,KSERIALIZER,VSERIALIZER,KDESERIALIZER,VDESERIALIZER);
        printMapCharacteristics(map);
        workOnKVMap(map);
    }

}
