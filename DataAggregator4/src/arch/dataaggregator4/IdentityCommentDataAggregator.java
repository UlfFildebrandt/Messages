package arch.dataaggregator4;

import java.util.ArrayList;
import java.util.List;

import arch.dataaggregatorinterface.DataAggregator;
import arch.datasourceinterface.DefaultDataItem;
import arch.datasourceinterface.IDataItem;
import arch.datasourceinterface.IDataSource;

final public class IdentityCommentDataAggregator extends DataAggregator {

    @Override
    public List<IDataItem> get() {
        List<IDataItem> itemList = new ArrayList<IDataItem>();

        for (int dataSourceIdx = 0; dataSourceIdx < this.dataSources.size(); dataSourceIdx++) {
            IDataSource dataSource = this.dataSources.get(dataSourceIdx);

            List<IDataItem> dataSourceList = dataSource.getData();
            for (int dataSourceItemsIdx = 0; dataSourceItemsIdx < dataSourceList.size(); dataSourceItemsIdx++) {
            	IDataItem item = new DefaultDataItem(dataSourceList.get(dataSourceItemsIdx));
            	if ( item.getCompany().equals("BMW") )
            		item.addValue("Luxus");
            	else
            		item.addValue("");
                itemList.add(item);
            }
        }

        return itemList;
    }

	@Override
	public String getType() {
		return "identitycomment";
	}
	
	@Override
	public List<String> getColumnHeaders() {
		List<String> headers = new ArrayList<String>();
		
		headers.add("Company");
		headers.add("Area");
		headers.add("Revenue");
		headers.add("Comment");
		
		return headers;
	}
}
