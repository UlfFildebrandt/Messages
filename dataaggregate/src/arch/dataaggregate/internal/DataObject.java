package arch.dataaggregate.internal;

import java.util.ArrayList;
import java.util.List;

import arch.datasourceinterface.IDataSource;

public class DataObject {
    private String type;
    private String title;

    private List<IDataSource> dataSources = new ArrayList<IDataSource>();

    public DataObject(String type, String title) {
        super();
        this.type = type;
        this.title = title;
    }

    public String getType() {
        return this.type;
    }

    public String getTitle() {
        return this.title;
    }

    public void addDataSource(IDataSource ds) {
        this.dataSources.add(ds);
    }
    
    public void addDataSources(List<IDataSource> ds) {
    	this.dataSources.addAll(ds);
    }

    public List<IDataSource> getDataSources() {
        return this.dataSources;
    }

}
