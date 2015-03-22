package main.java.com.betelguese.services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import main.java.com.betelguese.database.DBQuery;
import main.java.com.betelguese.database.DatabaseService;
import main.java.com.betelguese.utils.MessageBuilder;
import main.java.com.betelguese.utils.RequestName;
import main.java.com.betelguese.utils.items.YearReport;
import main.java.com.betelguese.utils.json.builders.YearReportMessage;

public class ReportService implements ServiceTag {
	private static final String REQUEST_NAME = RequestName.LOG_IN_REQUEST;
	private DatabaseService databaseService;
	private ResultSet resultSet;

	public ReportService() {

	}

	public String getReport(String serviceValue) {
		databaseService = new DatabaseService();
		System.out.println(serviceValue);
		if (serviceValue.equals("year")) {
			return yearService();
		}
		return null;
	}

	@SuppressWarnings("deprecation")
	private String yearService() {
		try {
			Date date = new Date(System.currentTimeMillis());
			databaseService.open();
			resultSet = databaseService.executeQuery(DBQuery.getMaxMinYear());
			if (resultSet.next()) {
				YearReportMessage yearReportMessage = new YearReportMessage(1,
						MessageBuilder.messageBuilder(SUCCESS_SERVICE,
								REQUEST_NAME), REQUEST_NAME);
				yearReportMessage
						.setMaxYear(resultSet.getString("max_year") == null ? Integer
								.toString(date.getYear() + 1900) : resultSet
								.getString("max_year").substring(0, 4));
				yearReportMessage
						.setMinYear(resultSet.getString("min_year") == null ? Integer
								.toString(date.getYear() + 1900) : resultSet
								.getString("min_year").substring(0, 4));
				int minYear = Integer.parseInt(yearReportMessage.getMinYear());
				int maxYear = Integer.parseInt(yearReportMessage.getMaxYear());
				if (maxYear == minYear) {
					yearReportMessage.setReports(singleYearService(maxYear));
				} else {
					yearReportMessage.setReports(multipleYearService(maxYear, minYear));
				}

				databaseService.close();
				Gson gson = new GsonBuilder().create();
				return gson.toJson(yearReportMessage);
			} else {
				YearReportMessage yearReportMessage = new YearReportMessage(1,
						MessageBuilder.messageBuilder(SUCCESS_SERVICE,
								REQUEST_NAME), REQUEST_NAME);
				yearReportMessage
						.setMaxYear(Integer.toString(date.getYear() + 1900));
				yearReportMessage
						.setMinYear(Integer.toString(date.getYear() + 1900));
				yearReportMessage.setReports(singleYearService(Integer
						.parseInt(yearReportMessage.getMaxYear())));
				databaseService.close();
				Gson gson = new GsonBuilder().create();
				return gson.toJson(yearReportMessage);
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
			return null;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return null;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	private List<YearReport> multipleYearService(int maxYear, int minYear)
			throws SQLException {
		List<YearReport> reports = new ArrayList<YearReport>();
		for (int i = minYear; i <= maxYear; i++) {
			YearReport yearReport = new YearReport();
			resultSet = databaseService.executeQuery(DBQuery
					.getYearTransactions(i));
			if (resultSet.next()) {
				yearReport.setYear(Integer.toString(i));
				yearReport.setValue(resultSet.getString("y" + i) == null ? "0"
						: resultSet.getString("y" + i));
			} else {
				yearReport.setYear(Integer.toString(i));
				yearReport.setValue("0");
			}
			reports.add(yearReport);
		}
		return reports;
	}

	private List<YearReport> singleYearService(int maxYear) throws SQLException {
		List<YearReport> reports = new ArrayList<YearReport>();
		YearReport yearReport = new YearReport();
		resultSet = databaseService.executeQuery(DBQuery
				.getYearTransactions(maxYear));
		if (resultSet.next()) {
			yearReport.setYear(Integer.toString(maxYear));
			yearReport
					.setValue(resultSet.getString("y" + maxYear) == null ? "0"
							: resultSet.getString("y" + maxYear));
		} else {
			yearReport.setYear(Integer.toString(maxYear));
			yearReport.setValue("0");
		}
		reports.add(yearReport);

		return reports;
	}

}
