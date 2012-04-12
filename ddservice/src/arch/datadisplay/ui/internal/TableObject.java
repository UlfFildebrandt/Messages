package arch.datadisplay.ui.internal;

import java.util.ArrayList;
import java.util.List;

public class TableObject {
    private String title;
    private StringBuilder buffer;

    private List<List<String>> list = null;

    public TableObject(List<List<String>> list, String title, StringBuilder buffer) {
        super();
        this.list = list;
        this.title = title;
        this.buffer = buffer;
    }

    public List<String> getItem(int i) {
    	return list.get(i);
    }
    
    public int getRows() {
    	return list.size();
    }
    
    public int getColumns() {
    	return list.get(0).size();
    }

    public String getTitle() {
        return this.title;
    }

    public StringBuilder getBuffer() {
        return this.buffer;
    }
}
