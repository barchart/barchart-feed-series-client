package com.barchart.feed.series.client.services;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import rx.Observable;

import com.barchart.feed.api.consumer.MarketService;
import com.barchart.feed.api.series.TimePoint;
import com.barchart.feed.api.series.TimeSeries;
import com.barchart.feed.api.series.services.HistoricalResult;
import com.barchart.feed.api.series.services.Query;
import com.barchart.feed.api.series.temporal.Period;
import com.barchart.feed.api.series.temporal.PeriodType;
import com.barchart.feed.client.provider.BarchartMarketProvider;
import com.barchart.feed.series.services.BarchartSeriesProvider;
import com.barchart.feed.series.services.BarchartHistoricalService;
import com.barchart.feed.series.services.QueryBuilder;

/**
 * <pre>
 * 		BarchartSeriesProvider provider = new BarchartSeriesProvider(MarketService);
 * 		Query query = QueryBuilder.create().symbol("ESZ3").build();
 * 		Observable<TimeSeries> series = provider.subscribe(query);
 * </pre>
 * @author David Ray
 *
 */
public class BarchartSeriesProviderTest {
	private BarchartSeriesProvider provider;
	FauxMarketService fms;
	
	public void testInstantiate() {
		BarchartMarketProvider marketService = new BarchartMarketProvider("dray", "dray");
		fms = new FauxMarketService("dray", "dray");
		BarchartHistoricalService<HistoricalResult> historicalService = new BarchartHistoricalService<HistoricalResult>("dray", "dray");
		//provider = new BarchartSeriesProvider(fms, historicalService);
		provider = new BarchartSeriesProvider(marketService, historicalService);
	}
	
	
	public Observable<TimeSeries<TimePoint>> testSubscribe() {
	    String qStr = "maxrecords=5&order=desc";
	    Query query = QueryBuilder.create().symbol("ESZ13").period(new Period(PeriodType.MINUTE, 1)).customQuery(qStr).build();
	    fms.preSubscribe(query);
	    
	    query = QueryBuilder.create().symbol("ESZ13").period(new Period(PeriodType.MINUTE, 1)).start(new DateTime().minusDays(3)).build();
		return provider.subscribe(query);
	}
	
	public static void main(String[] args) {
		BarchartSeriesProviderTest test = new BarchartSeriesProviderTest();
		test.testInstantiate();
		
		//Test that the market service daemon thread doesn't exit and our service stays up and running.
		try { 
			System.out.println("test wait before first subscription - 15secs.");
			for(int i = 1;i < 16;i++) {
				Thread.sleep(1000);
				System.out.print(i + " ");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("now testing subscribe");
		Observable<TimeSeries<TimePoint>> observable = test.testSubscribe();
		
	}
}
