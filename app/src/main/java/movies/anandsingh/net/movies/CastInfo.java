package movies.anandsingh.net.movies;

import java.io.Serializable;

/**
 * Created by Fadwasa on 07/07/2018 AD.
 */

public class CastInfo implements Serializable {
   private String cName,cPoster;
   int cID;

public void setcName(String cName){
    this.cName=cName;

}
public String getcName(){
    return cName;
}

    public void setcPoster(String cPoster){
        this.cPoster=cPoster;

    }
    public String getcPoster(){
        return cPoster;
    }
    public void setcID(int cID){
        this.cID=cID;

    }
    public int getcID(){
        return cID;
    }
}
