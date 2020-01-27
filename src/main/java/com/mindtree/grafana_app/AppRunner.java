package com.mindtree.grafana_app;

import java.util.concurrent.TimeUnit;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.influxdb.dto.Pong;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AppRunner {
	
	public static InfluxDB influxDB ;
	
	public static void main(String[] args){
		SpringApplication.run(AppRunner.class, args);
		System.out.println("Welcome to My first Maven Application on my laptop");
		
		InfluxDB influxDB = InfluxDBFactory.connect("http://localhost:8086/");
		///first_db
		

		//create database
		influxDB.createDatabase("second_db");
		//create default retention policy
		influxDB.createRetentionPolicy("defaultPolicy", "second_db", "10d", 1, true);
		
		//gives log event in detail
		influxDB.setLogLevel(InfluxDB.LogLevel.BASIC);
		
		Pong response = influxDB.ping();
		if (response.getVersion().equalsIgnoreCase("unknown")) {
		   System.out.println("Error pinging server.");
		    return;
		} 
		else{
			System.out.println("Connected");
		System.out.println(response);
		}
	
		//writeData();
		BatchPoints batchPoints = BatchPoints
				  .database("second_db")
				  .retentionPolicy("defaultPolicy")
				  .build();
				 
				Point point1 = Point.measurement("memory")
				  .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
				  .addField("name", "server1") 
				  .addField("free", 4743656L)
				  .addField("used", 1015096L) 
				  .addField("buffer", 1010467L)
				  .build();
				 
				Point point2 = Point.measurement("memory")
				  .time(System.currentTimeMillis() - 100, TimeUnit.MILLISECONDS)
				  .addField("name", "server1")
				  .addField("free", 4743696L)
				  .addField("used", 1016096L)
				  .addField("buffer", 1008467L)
				  .build();
				 
				batchPoints.point(point1);
				batchPoints.point(point2);
				influxDB.write(batchPoints);

	}

/*	private static void writeData() {
		BatchPoints batchPoints = BatchPoints
				  .database("second_db")
				  .retentionPolicy("defaultPolicy")
				  .build();
				 
				Point point1 = Point.measurement("memory")
				  .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
				  .addField("name", "server1") 
				  .addField("free", 4743656L)
				  .addField("used", 1015096L) 
				  .addField("buffer", 1010467L)
				  .build();
				 
				Point point2 = Point.measurement("memory")
				  .time(System.currentTimeMillis() - 100, TimeUnit.MILLISECONDS)
				  .addField("name", "server1")
				  .addField("free", 4743696L)
				  .addField("used", 1016096L)
				  .addField("buffer", 1008467L)
				  .build();
				 
				batchPoints.point(point1);
				batchPoints.point(point2);
				influxDB.write(batchPoints);

		
	}
	*/
}
