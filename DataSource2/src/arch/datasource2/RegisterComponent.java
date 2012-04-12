package arch.datasource2;

import arch.datasourceinterface.IDataSourceService;

public class RegisterComponent {
    
	private DataSource2 dataSource = null;
	
    protected void setDataSourceService(IDataSourceService dataSourceService) {
    	dataSource = new DataSource2();
    	dataSourceService.addDataSource(dataSource);     
    }

    protected void unsetDataSourceService(IDataSourceService dataSourceService) {
    	dataSourceService.removeDataSource(dataSource);
    	dataSource = null;
    }
}
