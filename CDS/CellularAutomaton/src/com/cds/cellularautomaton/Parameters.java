/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cds.cellularautomaton;

import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;

/**
 *
 * @author Jakub Piotrowski
 */
public class Parameters implements LookupListener
{
//    private final int POINTS_NUMBER = 300;
//    private final double topCenterLatitudeDifferent = 4.8403255;
//    private final double leftCenterLongitudeDifferent = 5.5447411;
//    private final double latitudeDifferent = 0.032268835;
//    private final double longitudeDifferent = 0.0369649405;
//    private Lookup.Result<Coordinates> centerCoordinates;
//    private HashMap<PointCoordinates, PointParameters> pointsHashMap;
//
//    public Parameters() 
//    {
//        pointsHashMap = new HashMap<PointCoordinates, PointParameters>();
//        centerCoordinates = WindowManager.getDefault().findTopComponent("MapParametersTopComponent").getLookup().lookupResult(Coordinates.class);
//        centerCoordinates.addLookupListener(this);
//    }
//    
//    
    @Override
    public void resultChanged(LookupEvent le) 
    {
//        double centerCoordinateLatitude = centerCoordinates.allInstances().iterator().next().getLatitude();
//        double centerCoordinateLongitude = centerCoordinates.allInstances().iterator().next().getLongitude();
//        double leftTopCoordinateLatitude = centerCoordinateLatitude + topCenterLatitudeDifferent;
//        double leftTopCoordinateLongitude = centerCoordinateLongitude - leftCenterLongitudeDifferent;
//
//        double tmpLatitude = leftTopCoordinateLatitude;
//        double tmpLongitude = leftTopCoordinateLongitude;
//
//
//        int counter = 0;
//        int resetCounter = 0;
//        int x = 0, y = 0;
//        int lowerBound = (int) Math.floor((POINTS_NUMBER*POINTS_NUMBER)/560);
//        while(counter < lowerBound)
//        {
//            double[][] points = new double[560][2];
//            for(int i = 0; i < 560; i++)
//            {
//                points[i][0] = tmpLatitude;
//                points[i][1] = tmpLongitude;
//                tmpLongitude += longitudeDifferent;
//                resetCounter++;
//                if(resetCounter % 300 == 0)
//                {
//                    tmpLongitude = leftTopCoordinateLongitude;
//                    tmpLatitude -= latitudeDifferent;
//                }
//            }
//            
//            Stock stock = new Stock(points);
//            
//            try 
//            {
//                CloseableHttpClient httpClient = HttpClientBuilder.create().build();
//                HttpPost request = new HttpPost("http://exeter.tk/coords2details");
//                StringEntity params = new StringEntity(new Gson().toJson(stock));
//                request.addHeader("content-type", "application/json");
//                request.setEntity(params);
//                HttpResponse result = httpClient.execute(request);
//                String json = EntityUtils.toString(result.getEntity(), "UTF-8");
//
//                System.out.println(json);
//                JSONArray array = new JSONArray(json);
//                for(int i = 0; i < array.length(); i++)
//                {
//                    JSONObject obj = array.getJSONObject(i);
//                    String terrain = obj.getJSONObject("statistics").getString("terrain");
//                    String value = obj.getJSONObject("statistics").getJSONObject("elevation").getString("value");
//                    boolean terrainBool = terrain.equalsIgnoreCase("land") ? true : false;
//
//                    System.out.println(terrain + " " + value);
//                    pointsHashMap.put(new PointCoordinates(x, y), new PointParameters(terrainBool, Integer.parseInt(value)));
//                    x++;
//                    if(x == 300)
//                    {
//                        x = 0;
//                        y++;
//                    }
//                }
//            } catch (UnsupportedEncodingException ex) {
//                Exceptions.printStackTrace(ex);
//            } catch (IOException ex) {
//                Exceptions.printStackTrace(ex);
//            } catch (JSONException ex) {
//                Exceptions.printStackTrace(ex);
//            }
//           
//           counter++;
//        }
//        
//        int lastPoints = (POINTS_NUMBER*POINTS_NUMBER) - (lowerBound * 560);
//        double[][] points = new double[lastPoints][2];
//        for(int i = 0; i < lastPoints; i++)
//        {
//            points[i][0] = tmpLatitude;
//            points[i][1] = tmpLongitude;
//            tmpLongitude += longitudeDifferent;
//            resetCounter++;
//            if(resetCounter % 300 == 0)
//            {
//                tmpLongitude = leftTopCoordinateLongitude;
//                tmpLatitude += latitudeDifferent;
//            }
//        }
//        Stock stock = new Stock(points);
//            
//        try 
//        {
//            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
//            HttpPost request = new HttpPost("http://exeter.tk/coords2details");
//            StringEntity params = new StringEntity(new Gson().toJson(stock));
//            request.addHeader("content-type", "application/json");
//            request.setEntity(params);
//            HttpResponse result = httpClient.execute(request);
//            String json = EntityUtils.toString(result.getEntity(), "UTF-8");
//
//            JSONArray array = new JSONArray(json);
//            for(int i = 0; i < array.length(); i++)
//            {
//                JSONObject obj = array.getJSONObject(i);
//                String terrain = obj.getJSONObject("statistics").getString("terrain");
//                String value = obj.getJSONObject("statistics").getJSONObject("elevation").getString("value");
//                boolean terrainBool = terrain.equalsIgnoreCase("land") ? true : false;
//
//                System.out.println(terrain + " " + value);
//                pointsHashMap.put(new PointCoordinates(x, y), new PointParameters(terrainBool, Integer.parseInt(value)));
//                x++;
//                if(x == 300)
//                {
//                    x = 0;
//                    y++;
//                }
//            }
//        } catch (UnsupportedEncodingException ex) {
//            Exceptions.printStackTrace(ex);
//        } catch (IOException ex) {
//            Exceptions.printStackTrace(ex);
//        } catch (JSONException ex) {
//            Exceptions.printStackTrace(ex);
//        }
//        
//        try
//        {
//            FileOutputStream fileOut = new FileOutputStream("D:\\Studia\\5 semestr\\Technologie agentowe\\Projekt\\CDS\\CDS\\CellularAutomaton\\src\\com\\cds\\serialization\\parametersHashMap.ser");
//            ObjectOutputStream out = new ObjectOutputStream(fileOut);
//            out.writeObject(pointsHashMap);
//            out.close();
//            fileOut.close();
//        }catch(Exception ex){}
    }
//    
//    private class Stock
//    {
//        double[][] locations;
//
//        public Stock(double[][] locations)
//        {
//                this.locations = locations;
//        }
//
//        public double[][] getLocation()
//        {
//                return locations;
//        }
//    }
    
}
