import java.math.BigDecimal;

public class LabelInfo {
	private String matNo;
	private String matNm;
	private BigDecimal packQty;

	public String getMatNo() {
		return matNo;
	}

	public void setMatNo(String matNo) {
		this.matNo = matNo;
	}

	public String getMatNm() {
		return matNm;
	}

	public void setMatNm(String matNm) {
		this.matNm = matNm;
	}

	public BigDecimal getPackQty() {
		return packQty;
	}

	public void setPackQty(BigDecimal packQty) {
		this.packQty = packQty;
	}
}
