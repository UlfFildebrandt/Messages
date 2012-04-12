package arch.datasource3;

import arch.datasourceinterface.IDataSourceService;

public class RegisterComponent {

	private DataSource3 dataSource = null;
	
    protected void setDataSourceService(IDataSourceService dataSourceService) {
    	dataSource = new DataSource3();
    	dataSourceService.addDataSource(dataSource);     
    }

    protected void unsetDataSourceService(IDataSourceService dataSourceService) {
    	dataSourceService.removeDataSource(dataSource);
    	dataSource = null;
    }
}
