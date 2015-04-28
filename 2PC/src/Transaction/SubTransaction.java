package Transaction;

import java.io.Serializable;

public class SubTransaction implements Serializable {
    String db_name;
    String query;

    public SubTransaction(String db_name, String query){
        this.db_name=db_name;
        this.query=query;
    }

    public SubTransaction(){
    }

    public String getDb_name() {
        return db_name;
    }

    public void setDb_name(String db_name) {
        this.db_name = db_name;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

}