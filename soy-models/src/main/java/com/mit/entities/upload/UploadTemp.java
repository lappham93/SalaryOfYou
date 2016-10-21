package com.mit.entities.upload;

import java.util.ArrayList;
import java.util.List;

public class UploadTemp {
	private long id;
	private int type;
	private int total;
	private int size;
	private int totalComplete;
	private int sizeComplete;
	private List<Boolean> check;
	private List<byte[]> data;

	public UploadTemp(long id, int type, int total, int size) {
		this.id = id;
		this.type = type;
		this.total = total;
		this.size = size;
		check  = new ArrayList<Boolean>(total);
		data =  new ArrayList<byte[]>(total);
		for(int i = 0; i < total; i++) {
			data.add(new byte[0]);
			check.add(false);
		}
	}

	public UploadTemp(long id, int type, int total, int size,
			int totalComplete, int sizeComplete, List<Boolean> check) {
		super();
		this.id = id;
		this.type = type;
		this.total = total;
		this.size = size;
		this.totalComplete = totalComplete;
		this.sizeComplete = sizeComplete;
		this.check = check;
	}



	public UploadTemp(long id, int type, int total, int size,
			int totalComplete, int sizeComplete, List<Boolean> check,
			List<byte[]> data) {
		super();
		this.id = id;
		this.type = type;
		this.total = total;
		this.size = size;
		this.totalComplete = totalComplete;
		this.sizeComplete = sizeComplete;
		this.check = check;
		this.data = data;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public List<Boolean> getCheck() {
		return check;
	}

	public void setCheck(List<Boolean> check) {
		this.check = check;
	}

	public List<byte[]> getData() {
		return data;
	}

	public void setData(List<byte[]> data) {
		this.data = data;
	}

	public int getTotalComplete() {
		return totalComplete;
	}

	public void setTotalComplete(int totalComplete) {
		this.totalComplete = totalComplete;
	}

	public int getSizeComplete() {
		return sizeComplete;
	}

	public void setSizeComplete(int sizeComplete) {
		this.sizeComplete = sizeComplete;
	}

    @Override
    public String toString() {
        return "UploadTemp{" + "id=" + id + ", type=" + type + ", total=" + total + ", size=" + size + ", totalComplete=" + totalComplete + ", sizeComplete=" + sizeComplete + ", check=" + check + ", data=" + data + '}';
    }
    
    
}
