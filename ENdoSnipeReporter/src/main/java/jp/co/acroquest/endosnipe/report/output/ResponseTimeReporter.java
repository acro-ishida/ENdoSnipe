/*
 * Copyright (c) 2004-2009 SMG Co., Ltd. All Rights Reserved.
 * Please read the associated COPYRIGHTS file for more details.
 *
 * THE  SOFTWARE IS  PROVIDED BY  SMG Co., Ltd., WITHOUT  WARRANTY  OF
 * ANY KIND,  EXPRESS  OR IMPLIED,  INCLUDING BUT  NOT LIMITED  TO THE
 * WARRANTIES OF  MERCHANTABILITY,  FITNESS FOR A  PARTICULAR  PURPOSE
 * AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDER BE LIABLE FOR ANY
 * CLAIM, DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */
package jp.co.acroquest.endosnipe.report.output;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import jp.co.acroquest.endosnipe.report.entity.ResponseTimeRecord;
import jp.co.acroquest.endosnipe.report.output.ResponseTimeReporter;

import org.bbreak.excella.reports.exporter.ExcelExporter;
import org.bbreak.excella.reports.model.ReportBook;
import org.bbreak.excella.reports.model.ReportSheet;
import org.bbreak.excella.reports.processor.ReportProcessor;
import org.bbreak.excella.reports.tag.BlockRowRepeatParamParser;
import org.bbreak.excella.reports.tag.RowRepeatParamParser;
import org.bbreak.excella.reports.tag.SingleParamParser;

/**
 * レスポンスタイムのレポートを出力するクラス
 * 
 * @author Y.Ochiai
 * 
 */
public class ResponseTimeReporter
{

    /** 参照するテンプレートのシート名 */
    public static final String TEMPLATE_SHEET_NAME  = "レスポンスタイムレポート";

    /** 出力シート名 */
    public static final String OUTPUT_SHEET_NAME    = "レスポンスタイムレポート";

    /** 置換パラメータ名 */
    public static final String RESPONSE_TIME_RECORD = "responseTimeRecord";

    /** 項番を表すパラメータ名 */
    public static final String NUMBERS              = "numbers";

    /** 開始時刻を表示するパラメータ名 */
    public static final String DATA_RANGE           = "dataRange";

    /**
     * レポートのエクセルファイルを出力する
     * 
     * @param templateFilePath
     *            テンプレートファイルのパス
     * @param outputFilePath
     *            出力するファイルのパス
     * @param records
     *            出力するデータのリスト
     * @param startDate
     *            開始日時
     * @param endDate
     *            終了日時
     */
    public void outputReport(String templateFilePath, String outputFilePath,
            ResponseTimeRecord[] records, Date startDate, Date endDate)
    {
        // ?読み込むテンプレートファイルのパス(拡張子含)
        // ?出力先のファイルパス(拡張子はExporterによって自動的に付与されるため、不要。)
        // ?ファイルフォーマット(ConvertConfigurationの配列)
        // を指定し、ReportBookインスタンスを生成する。
        ReportBook outputBook =
                                new ReportBook(templateFilePath, outputFilePath,
                                               ExcelExporter.FORMAT_TYPE);

        // テンプレートファイル内のシート名と出力シート名を指定し、
        // ReportSheetインスタンスを生成して、ReportBookに追加します。
        ReportSheet outputDataSheet =
                                      new ReportSheet(ResponseTimeReporter.TEMPLATE_SHEET_NAME,
                                                      ResponseTimeReporter.OUTPUT_SHEET_NAME);
        outputBook.addReportSheet(outputDataSheet);

        // 置換パラメータをReportSheetオブジェクトに追加する。
        // (反復置換のパラメータには配列を渡す。)
        List<Integer> numberList = new ArrayList<Integer>();
        for (int index = 0; index < records.length; index++)
        {
            numberList.add(index + 1);
        }
        outputDataSheet.addParam(BlockRowRepeatParamParser.DEFAULT_TAG,
                                 ResponseTimeReporter.RESPONSE_TIME_RECORD, records);

        //表の一番左端の列に項目番号を追加
        outputDataSheet.addParam(RowRepeatParamParser.DEFAULT_TAG, ResponseTimeReporter.NUMBERS,
                                 numberList.toArray());

        //○月○日(○) ○○:○○ から  ○月○日(○) ○○:○○ までのデータ取得結果です
        //という文字列を表示させる
        String dataRange = this.getDataRangeString(startDate, endDate);
        outputDataSheet.addParam(SingleParamParser.DEFAULT_TAG, ResponseTimeReporter.DATA_RANGE,
                                 dataRange);

        // 
        // ReportProcessorインスタンスを生成し、
        // ReportBookを元にレポート処理を実行します。
        // 
        ReportProcessor reportProcessor = new ReportProcessor();
        try
        {
            reportProcessor.process(outputBook);
        }
        catch (Exception e)
        {
            // 出力失敗
            e.printStackTrace();
        }
    }

    /**
     * データ取得時刻の範囲を表示する文字列を成型する
     * @param startDate データ取得開始日時
     * @param endDate データ取得終了日時
     * @return　表示用の文字列
     */
    private String getDataRangeString(Date startDate, Date endDate)
    {
        Calendar calendar = Calendar.getInstance();

        //データ取得開始日時とデータ取得終了日時を成型する
        calendar.setTime(startDate);
        String startDateString = String.format("%1$tY/%1$tm/%1$td(%1$ta) %1$tH:%1$tM", calendar);
        calendar.setTime(endDate);
        String endDateString = String.format("%1$tY/%1$tm/%1$td(%1$ta) %1$tH:%1$tM", calendar);

        //表示用文字列を成型する
        StringBuilder builder = new StringBuilder();
        builder.append("Result of data acquisition from ");
        builder.append(startDateString);
        builder.append(" to ");
        builder.append(endDateString);

        String returnValue = builder.toString();

        return returnValue;
    }

}
