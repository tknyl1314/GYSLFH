package proj4j.util;

import java.util.HashMap;
import java.util.Map;

import proj4j.CRSFactory;
import proj4j.CoordinateReferenceSystem;
import proj4j.InvalidValueException;
import proj4j.UnknownAuthorityCodeException;
import proj4j.UnsupportedParameterException;

public class CRSCache 
{
  private static Map<String, CoordinateReferenceSystem> projCache = new HashMap<String, CoordinateReferenceSystem>();
  private static CRSFactory crsFactory = new CRSFactory();

// TODO: provide limit on number of items in cache (LRU)
  
  public CRSCache() {
    super();
  }

  public CoordinateReferenceSystem createFromName(String name)
  throws UnsupportedParameterException, InvalidValueException, UnknownAuthorityCodeException
  {
    CoordinateReferenceSystem proj = (CoordinateReferenceSystem) projCache.get(name);
    if (proj == null) {
      proj = crsFactory.createFromName(name);
      projCache.put(name, proj);
    }
    return proj;
  }

}
