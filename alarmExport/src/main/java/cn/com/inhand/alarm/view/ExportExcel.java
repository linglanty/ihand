package cn.com.inhand.alarm.view;

import cn.com.inhand.alarm.service.*;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress; 
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import cn.com.inhand.alarm.dto.AlarmBean;
import cn.com.inhand.dn4.utils.DateUtils;

@Service
public class ExportExcel extends AbstractExcelView {

//	@Autowired
//	private AlarmService alarmService;
    private static final String EXCELTITLE = "告警报表";
    private static final String[] HEADS = new String[]{"级别", "告警来源", "现场名称",
        "状态", "描述", "告警时间"};

    private static final String SERVER_ALARM = "严重警告";
    private static final String IMPORTANT_ALARM = "重要警告";
    private static final String MINOR_ALARM = "次要警告";
    private static final String WARN = "警告";
    private static final String TIP = "提醒";

    private static final String AFFIRMED = "已确认";
    private static final String NOT_AFFIRMED = "未确认";
    private static final String CLEARED = "已清除";

    private static final String ERROR = "error";

    @Override
    public void buildExcelDocument(Map<String, Object> model,
            HSSFWorkbook workbook, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        response.setCharacterEncoding("utf-8");
        response.setContentType("APPLICATION/OCTET-STREAM");
        response.setHeader("Content-disposition", "attachment;filename=alarmexcel.xls");
		// 导出报表
        // 生成excel开始
        // 声明一个工作薄
//		HSSFWorkbook workbook = new HSSFWorkbook();
        // 生成一个表格
        HSSFSheet sheet = workbook.createSheet("告警报表");
        // 设置表格默认列宽度为15个字节
        sheet.setDefaultColumnWidth(15);
        // 生成一个样式
        HSSFCellStyle style = workbook.createCellStyle();
        // 设置这些样式
        style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        // 生成一个字体
        HSSFFont font = workbook.createFont();
        font.setColor(HSSFColor.VIOLET.index);
        font.setFontHeightInPoints((short) 12);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 把字体应用到当前的样式
        style.setFont(font);
        // 生成并设置另一个样式
        HSSFCellStyle style2 = workbook.createCellStyle();
        style2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
        style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        // 生成另一个字体
        HSSFFont font2 = workbook.createFont();
        font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        // 把字体应用到当前的样式
        style2.setFont(font2);
		// -------------------------------------以下写入excel-------------------------------------
        // 级别----告警来源----现场名称----状态----描述-----告警时间
        HSSFRow titleRow = sheet.createRow(0);
        HSSFCell titleCell = titleRow.createCell(0);
        titleCell.setCellValue(EXCELTITLE);
        titleCell.setCellStyle(style2);// 设置标题样式
        // 添加表头
        HSSFRow headRow = sheet.createRow(1);// 表头行
        for (int i = 0; i < HEADS.length; i++) {
            HSSFCell headCell = headRow.createCell(i);
            headCell.setCellValue(HEADS[i]);// 设置每个表头数据
        }
        // 表头合并单元格
        sheet.addMergedRegion(new CellRangeAddress(0, // first row (0-based)
                0, // last row (0-based)
                0, // first column (0-based)
                HEADS.length - 1 // last column (0-based)
        ));
        int index = 0;// alarmList的索引 
        //获取alarmList
        List<AlarmBean> alarmList =  (List<AlarmBean>)model.get("alarmList");
//        alarmService.getAlarmData((String) model.get("params"), (ObjectId) model.get("xOId"));

        // 遍历数据从第二行开始
        for (int i = 2; i < alarmList.size() + 2; i++) {
            index = i - 2;
            HSSFRow dataRow = sheet.createRow(i);
            for (int j = 0; j < HEADS.length; j++) {
                HSSFCell dataCell = dataRow.createCell(j);
                dataCell.setCellStyle(style2);// 设置表格字体样式
                switch (j) {
                    case 0:// 级别
                        dataCell.setCellValue(convertLevel(alarmList.get(index)
                                .getLevel()));
                        break;
                    case 1:// 告警来源
                        dataCell.setCellValue(alarmList.get(index).getSourceName());
                        break;
                    case 2:// 现场名称
                        dataCell.setCellValue(alarmList.get(index).getSiteName());
                        break;
                    case 3:// 状态
                        dataCell.setCellValue(this.converStatu(alarmList.get(index)
                                .getState()));
                        break;
                    case 4:// 描述
                        dataCell.setCellValue(alarmList.get(index).getDesc());
                        break;
                    case 5:// 告警时间
                        dataCell.setCellValue(DateUtils.dateFormat(alarmList.get(
                                index).getTimestamp()));
                        break;
                    default:
                        dataCell.setCellValue("error");
                }
            }
        }
		// -------------------------------------以上写入excel结束-------------------------------------

 
    }

    /**
     * 将int型数据转换成中文输出
     *
     * @param level
     * @return
     */
    private String convertLevel(int level) {
        String confirm = "";
        switch (level) {
            case Level.SERVER_ALARM:
                confirm = SERVER_ALARM;
                break;
            case Level.IMPORTANT_ALARM:
                confirm = IMPORTANT_ALARM;
                break;
            case Level.MINOR_ALARM:
                confirm = MINOR_ALARM;
                break;
            case Level.WARN:
                confirm = WARN;
                break;
            case Level.TIP:
                confirm = TIP;
                break;
            default:
                confirm = ERROR;
        }
        return confirm;
    }

    /**
     * 将int型status转换成对应的String类型
     *
     * @param status
     * @return
     */
    private String converStatu(int status) {
        String statuStr = "";
        switch (status) {
            case Statu.AFFIRMED:
                statuStr = AFFIRMED;
                break;
            case Statu.NOT_AFFIRMED:
                statuStr = NOT_AFFIRMED;
                break;
            case Statu.CLEARED:
                statuStr = CLEARED;
                break;
            default:
                statuStr = ERROR;
        }
        return statuStr;
    }

}
