import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class LabelPrint {
	private static Logger loger = LogManager.getLogger(LabelPrint.class);
	
	private List<LabelInfo> labelInfos = null;
	private String templateFile = null;
	private String outputFile = null;
	
	public LabelPrint() {
		this.labelInfos = new ArrayList<>();
		this.templateFile = "LabelTemplate.jasper";
		this.outputFile = "./LabelPrint.pdf";
	}
	
	private void processLabelData() {
		JFileChooser chooser = new JFileChooser("D:");
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel Files", "xlsx");
		chooser.setFileFilter(filter);
		chooser.setApproveButtonText("選擇標簽資料");

		int returnVal = chooser.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			try (FileInputStream inputStream = new FileInputStream(chooser.getSelectedFile());
					Workbook workbook = new XSSFWorkbook(inputStream);) {

				Sheet firstSheet = workbook.getSheetAt(0);
				DataFormatter formatter = new DataFormatter();
				for (Row row : firstSheet) {
					if (row.getRowNum() == 0)
						continue;
					
					int labelQty = Integer.parseInt(formatter.formatCellValue(row.getCell(3)));
					
					for(int i=1;i<=labelQty;i++) {
						LabelInfo labelInfo = new LabelInfo();
						labelInfo.setMatNo(formatter.formatCellValue(row.getCell(0)));
						labelInfo.setMatNm(formatter.formatCellValue(row.getCell(1)));
						labelInfo.setPackQty(new BigDecimal(formatter.formatCellValue(row.getCell(2))));
						
						labelInfos.add(labelInfo);
					}
				}

			} catch (Exception e) {
				loger.error("",e);
				System.exit(1);
			}
		}
	}
	
	private void exportFile() {
		if (!labelInfos.isEmpty()) {
			try{
				JasperPrint jasperPrint = JasperFillManager.fillReport(LabelPrint.class.getClassLoader()
						.getResourceAsStream(templateFile),null,new JRBeanCollectionDataSource(labelInfos));
				JasperExportManager.exportReportToPdfFile(jasperPrint,outputFile);
			} catch (JRException e) {
				loger.error("exportFile",e);
			}
		}
	}

	public static void main(String[] args){
		LabelPrint printer = new LabelPrint();
		
		printer.processLabelData();
		printer.exportFile();
	}
}
