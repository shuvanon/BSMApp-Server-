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
import main.java.com.betelguese.utils.RequestName.ReportRequest;
import main.java.com.betelguese.utils.helpers.Constants;
import main.java.com.betelguese.utils.helpers.Log;
import main.java.com.betelguese.utils.items.MonthReport;
import main.java.com.betelguese.utils.items.ReportServiceValue;
import main.java.com.betelguese.utils.items.YearReport;
import main.java.com.betelguese.utils.json.builders.MonthReportMessage;
import main.java.com.betelguese.utils.json.builders.ServiceMessage;
import main.java.com.betelguese.utils.json.builders.YearReportMessage;

public class ReportService implements ServiceTag, ReportRequest {
	private static final String TAG = ReportService.class.getSimpleName();
	private static final String REQUEST_NAME = RequestName.REPORT_REQUEST;
	private DatabaseService databaseService;
	private ResultSet resultSet;

	public ReportService() {

	}

	public String getReport(String serviceKey, String serviceValue) {
		databaseService = new DatabaseService();
		System.out.println(serviceKey);
		Gson gson = new GsonBuilder().create();
		ReportServiceValue reportServiceValue = gson.fromJson(serviceValue,
				ReportServiceValue.class);
		if (serviceKey.equals(YEARLY_SERVICE_KEY)) {
			if (reportServiceValue.getServiceName().equals("year")
					&& reportServiceValue.getYearValue() == null
					&& reportServiceValue.getMonthValue() == null) {
				return yearService();
			} else {
				return serverErrorMessage(new Exception("Error Service Value"));
			}
		} else if (serviceKey.equals(MONTHLY_SERVICE_KEY)) {
			if (reportServiceValue.getServiceName().equals("month")
					&& reportServiceValue.getYearValue() != null
					&& reportServiceValue.getMonthValue() == null) {
				return monthService(reportServiceValue.getYearValue());
			} else {
				return serverErrorMessage(new Exception("Error Service Value"));
			}
		} else if (serviceKey.equals(DAILY_SERVICE_KEY)) {
			if (reportServiceValue.getServiceName().equals("day")
					&& reportServiceValue.getYearValue() != null
					&& reportServiceValue.getMonthValue() != null) {
				return dayService(reportServiceValue.getYearValue(),
						reportServiceValue.getMonthValue());
			} else {
				return serverErrorMessage(new Exception("Error Service Value"));
			}
		} else if (serviceKey.equals(TRANSACTION_SERVICE_KEY)) {
			if (serviceValue.equals("year")) {
				return yearService();
			} else {
				return serverErrorMessage(new Exception("Error Service Value"));
			}
		} else if (serviceKey.equals(GET_YEAR_SERVICE_KEY)) {
			if (reportServiceValue.getServiceName().equals("getYear")
					&& reportServiceValue.getYearValue() == null
					&& reportServiceValue.getMonthValue() == null) {
				return getYearService();
			} else {
				return serverErrorMessage(new Exception("Error Service Value"));
			}
		} else {
			return serverErrorMessage(new Exception("Error Service Value"));
		}
	}

	private String getYearService() {
		YearReportMessage yearReportMessage = getMaxMinYear();
		Gson gson = new GsonBuilder().create();
		return gson.toJson(yearReportMessage);
	}

	@SuppressWarnings("deprecation")
	private YearReportMessage getMaxMinYear() {
		try {
			Date date = new Date(System.currentTimeMillis());
			YearReportMessage yearReportMessage = new YearReportMessage(1,
					MessageBuilder
							.messageBuilder(SUCCESS_SERVICE, REQUEST_NAME),
					REQUEST_NAME);
			databaseService.open();
			resultSet = databaseService.executeQuery(DBQuery.getMaxMinYear());
			if (resultSet.next()) {
				yearReportMessage
						.setMaxYear(resultSet.getString("max_year") == null ? Integer
								.toString(date.getYear() + 1900) : resultSet
								.getString("max_year").substring(0, 4));
				yearReportMessage
						.setMinYear(resultSet.getString("min_year") == null ? Integer
								.toString(date.getYear() + 1900) : resultSet
								.getString("min_year").substring(0, 4));
				databaseService.close();
				return yearReportMessage;
			} else {
				yearReportMessage
						.setMaxYear(Integer.toString(date.getYear() + 1900));
				yearReportMessage
						.setMinYear(Integer.toString(date.getYear() + 1900));
				yearReportMessage.setReports(singleYearService(Integer
						.parseInt(yearReportMessage.getMaxYear())));
				databaseService.close();
				return yearReportMessage;
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

	private String dayService(String yearValue, String monthValue) {
		return null;
	}

	private String monthService(String year) {
		try {
			databaseService.open();
			MonthReportMessage monthReportMessage = new MonthReportMessage(1,
					MessageBuilder
							.messageBuilder(SUCCESS_SERVICE, REQUEST_NAME),
					REQUEST_NAME);
			List<MonthReport> reports = new ArrayList<MonthReport>();
			for (int i = 0; i < 11; i++) {
				MonthReport monthReport = new MonthReport();
				String month = Integer.toString(i + 1).length() == 1 ? "0"
						+ (i + 1) : Integer.toString(i + 1);
				resultSet = databaseService.executeQuery(DBQuery
						.getMonthTransactions(year, month, Constants.month[i]));
				monthReport.setMonth(Constants.month[i]);
				resultSet.next();
				monthReport.setValue(resultSet.getString(Constants.month[i]
						+ "_" + month) == null ? "0" : resultSet
						.getString(Constants.month[i] + "_" + month));

				reports.add(monthReport);
			}
			monthReportMessage.setReports(reports);
			Gson gson = new GsonBuilder().create();
			return gson.toJson(monthReportMessage);
		} catch (InstantiationException e) {
			return serverErrorMessage(e);
		} catch (IllegalAccessException e) {
			return serverErrorMessage(e);
		} catch (ClassNotFoundException e) {
			return serverErrorMessage(e);
		} catch (SQLException e) {
			return serverErrorMessage(e);
		}
	}

	private String yearService() {
		try {
			YearReportMessage yearReportMessage = getMaxMinYear();
			databaseService.open();
			int minYear = Integer.parseInt(yearReportMessage.getMinYear());
			int maxYear = Integer.parseInt(yearReportMessage.getMaxYear());
			if (maxYear == minYear) {
				yearReportMessage.setReports(singleYearService(maxYear));
			} else {
				yearReportMessage.setReports(multipleYearService(maxYear,
						minYear));
			}
			Gson gson = new GsonBuilder().create();
			return gson.toJson(yearReportMessage);

		} catch (InstantiationException e) {
			e.printStackTrace();
			return serverErrorMessage(e);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return serverErrorMessage(e);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return serverErrorMessage(e);
		} catch (SQLException e) {
			e.printStackTrace();
			return serverErrorMessage(e);
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

	private String serverErrorMessage(Exception e) {
		Log.e(TAG, e.getClass().getSimpleName() + " occured" + e.getMessage(),
				e);
		ServiceMessage serviceMessage = new ServiceMessage(0,
				MessageBuilder.messageBuilder(SERVER_ERROR, REQUEST_NAME),
				REQUEST_NAME);
		Gson gson = new GsonBuilder().create();
		return gson.toJson(serviceMessage);
	}

}
