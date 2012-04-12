package arch.dataaggregate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import arch.dataaggregate.internal.DataObject;
import arch.dataaggregatorinterface.IDataAggregator;
import arch.dataaggregatorinterface.IDataAggregator2;
import arch.datasourceinterface.IDataItem;
import arch.datasourceinterface.IDataSource;
import arch.datasourceinterface.IDataSourceService;

public class DataAggregate implements IDataAggregate {

    /**
	 * 
	 */
    private IDataSourceService dataSourceService = null;
    
    protected void setDataSourceService(IDataSourceService ds) {
    	dataSourceService = ds;
    }

    protected void unsetDataSourceService(IDataSourceService dataSourceService) {
    }

    private Map<String, IDataAggregator> map = null;
    
    public void setMap(Map<String, IDataAggregator> cc) {
    	this.map = cc;
    }

    public List<List<String>> getData(String type) {
        List<IDataSource> list = dataSourceService.getDataSources();

        DataObject analyzedData = new DataObject(type, "");
        analyzedData.addDataSources(list);
        return generateData(analyzedData);
    }
    
    private IDataAggregator getAggregator(String type) {
    	return map.get(type);
    }

    private List<List<String>> generateData(DataObject tableObject) {
    	List<List<String>> list = new ArrayList<List<String>>();
    	
        IDataAggregator aggregator = getAggregator(tableObject.getType());
        aggregator.addDataSource(tableObject.getDataSources());

        List<IDataItem> itemList = aggregator.get();
        
        if ( aggregator instanceof IDataAggregator2 ) {
        	IDataAggregator2 aggregator2 = (IDataAggregator2)aggregator;
            List<String> headers = aggregator2.getColumnHeaders();
            list.add(headers);   	
        } else {
        	List<String> headers = new ArrayList<String>();
        	for(int i = 0; i < 2; i++)
        		headers.add("");
        	list.add(headers);
        }

        for (int rowIdx = 0; rowIdx < itemList.size(); rowIdx++) {
            List<String> item = generateDataRow(itemList, rowIdx);
            list.add(item);
        }
        
        return list;
    }

    private List<String> generateDataRow(List<IDataItem> list1, int i) {
    	List<String> list = new ArrayList<String>();
    	list.add(list1.get(i).getCompany());
    	list.add(list1.get(i).getArea());
    	list.add(Integer.toString(list1.get(i).getRevenue()));
    	
    	List<String> values = list1.get(i).getValues();
    	for(String value : values)
    		list.add(value);

    	return list;
    }
}
