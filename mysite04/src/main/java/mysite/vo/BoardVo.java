package mysite.vo;

public class BoardVo {
	private long id;
	private String title;
	private String contents;
	private long hit;
	private String regDate;
	private long groupNo;
	private long orderNo;
	private long depth;
	private long userId;
	private String userName;

	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getContents() {
		return contents;
	}
	
	public void setContents(String contents) {
		this.contents = contents;
	}
	
	public long getHit() {
		return hit;
	}
	
	public void setHit(long hit) {
		this.hit = hit;
	}
	
	public String getRegDate() {
		return regDate;
	}
	
	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}
	
	public long getGroupNo() {
		return groupNo;
	}
	
	public void setGroupNo(long groupNo) {
		this.groupNo = groupNo;
	}
	
	public long getOrderNo() {
		return orderNo;
	}
	
	public void setOrderNo(long orderNo) {
		this.orderNo = orderNo;
	}
	
	public long getDepth() {
		return depth;
	}
	
	public void setDepth(long depth) {
		this.depth = depth;
	}
	
	public long getUserId() {
		return userId;
	}
	
	public void setUserId(long userId) {
		this.userId = userId;
	}
	
	// needs to update
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
