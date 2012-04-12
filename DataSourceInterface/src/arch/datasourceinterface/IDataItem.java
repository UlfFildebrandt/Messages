package arch.datasourceinterface;

import java.util.List;

public interface IDataItem {
    public String getCompany();

    public int getRevenue();

    public String getArea();

    public void setArea(String area);
    
    public List<String> getValues();
    
    public void addValue(String s);
}
