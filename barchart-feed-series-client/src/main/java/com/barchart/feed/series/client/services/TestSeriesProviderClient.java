package com.barchart.feed.series.client.services;

import org.joda.time.DateTime;

import rx.Observer;

import com.barchart.feed.api.series.Period;
import com.barchart.feed.api.series.PeriodType;
import com.barchart.feed.api.series.Span;
import com.barchart.feed.api.series.network.NetworkObservable;
import com.barchart.feed.api.series.network.Query;
import com.barchart.feed.api.series.service.HistoricalResult;
import com.barchart.feed.client.provider.BarchartMarketProvider;
import com.barchart.feed.series.network.BarchartSeriesProvider;
import com.barchart.feed.series.network.QueryBuilderImpl;
import com.barchart.feed.series.service.BarchartFeedService;
import com.barchart.feed.series.service.BarchartHistoricalService;

/**
 * <pre>
 * 		BarchartSeriesProvider provider = new BarchartSeriesProvider(MarketService);
 * 		Query query = QueryBuilder.create().symbol("ESZ3").build();
 * 		Observable<TimeSeries> series = provider.subscribe(query);
 * </pre>
 * @author David Ray
 *
 */
public class TestSeriesProviderClient {
	private BarchartSeriesProvider provider;
	
	
	public void testInstantiate() {
		BarchartMarketProvider marketService = new BarchartMarketProvider("dray", "dray");
		BarchartHistoricalService<HistoricalResult> historicalService = new BarchartHistoricalService<HistoricalResult>("dray", "dray");
		BarchartFeedService feed = new BarchartFeedService(marketService, historicalService);
		provider = new BarchartSeriesProvider(feed);
	}
	
	public NetworkObservable testSubscribe() {
		Query query = QueryBuilderImpl.create().symbol("ESZ13").period(new Period(PeriodType.MINUTE, 1)).start(new DateTime().minusDays(3)).build();
		return provider.fetch(query);
	}
	
	public static void main(String[] args) {
		TestSeriesProviderClient test = new TestSeriesProviderClient();
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
		NetworkObservable observable = test.testSubscribe();
		if(observable != null) {
//			observable.count(); //Do nothing
//			observable.subscribe(new Observer<Span>() {
//			    @Override public void onCompleted() {}
//			    @Override public void onError(Throwable e) {}
//			    @Override
//                public void onNext(Span args) {
//                    System.out.println("TimeSeries got update");
//                }
//			});
		}
	}
}
