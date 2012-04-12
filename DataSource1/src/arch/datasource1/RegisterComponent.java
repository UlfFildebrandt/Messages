package arch.datasource1;

import arch.datasourceinterface.IDataSourceService;

public class RegisterComponent {
	
	private DataSource1 dataSource = null;
	
    protected void setDataSourceService(IDataSourceService dataSourceService) {
    	dataSource = new DataSource1();
    	dataSourceService.addDataSource(dataSource);     
    }

    protected void unsetDataSourceService(IDataSourceService dataSourceService) {
    	dataSourceService.removeDataSource(dataSource);
    	dataSource = null;
    }
}
