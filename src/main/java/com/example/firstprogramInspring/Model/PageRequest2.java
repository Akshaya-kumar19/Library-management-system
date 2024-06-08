package com.example.firstprogramInspring.Model;

import java.util.HashMap;
import java.util.List;

public class PageRequest2 {
	private int draw;
    private List<HashMap<String, Object>> columns;
    private List<HashMap<String, Object>> order;
    private HashMap<String, Object> search;
    private int start;
    private int length;

    public int getDraw() {
        return draw;
    }
    public void setDraw(int draw) {
        this.draw = draw;
    }
    public List<HashMap<String, Object>> getColumns() {
        return columns;
    }
    public void setColumns(List<HashMap<String, Object>> columns) {
        this.columns = columns;
    }
    public List<HashMap<String, Object>> getOrder() {
        return order;
    }
    public void setOrder(List<HashMap<String, Object>> order) {
        this.order = order;
    }
    public int getStart() {
        return start;
    }
    public void setStart(int start) {
        this.start = start;
    }
    public int getLength() {
        return length;
    }
    public void setLength(int length) {
        this.length = length;
    }
	public HashMap<String, Object> getSearch() {
		return search;
	}
	public void setSearch(HashMap<String, Object> search) {
		this.search = search;
	}

}

