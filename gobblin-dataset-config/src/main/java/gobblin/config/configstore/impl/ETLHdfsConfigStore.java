package gobblin.config.configstore.impl;

import java.net.URI;
import java.util.Collections;
import java.util.Collection;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import gobblin.config.configstore.ConfigStoreWithResolution;
import gobblin.config.configstore.VersionComparator;

public class ETLHdfsConfigStore extends HdfsConfigStoreWithOwnInclude implements ConfigStoreWithResolution{

  public final static String DATASET_PREFIX = "datasets";
  public final static String TAG_PREFIX = "tags";
  public final static String ID_DELEMETER = "/";
  
  public ETLHdfsConfigStore(String scheme, String location, VersionComparator<String> vc) {
    super(scheme, location, vc);
  }

  /**
   * 
   */
  private static final long serialVersionUID = 8102621120827801365L;

  @Override
  public Config getResolvedConfig(URI uri) {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public URI getParent(URI uri){
    if(isValidURI(uri)){
      return super.getParent(uri);
    }
    return null;
  }
  
  @Override
  public Collection<URI> getChildren(URI uri){
    if(isValidURI(uri)){
      return super.getChildren(uri);
    }
    return Collections.emptyList();
  }
  
  @Override
  public Collection<URI> getImports(URI uri){
    if(!isValidURI(uri)){
      return Collections.emptyList();
    }
    
    Collection<URI> superRes = super.getImports(uri);
    for(URI i: superRes){
      // can not import datasets
      if(isValidDataset(i)){
        throw new RuntimeException(String.format("URI %s Can not import dataset %s", uri.toString(), i.toString()));
      }
    }
    
    return superRes;
  }
  
  @Override
  public Config getOwnConfig(URI uri){
    if(isValidURI(uri)){
      return super.getOwnConfig(uri);
    }
    return ConfigFactory.empty();
  }
  
  public static final boolean isValidURI(URI uri) {
    return isValidTag(uri) || isValidDataset(uri);
  }

  public static final boolean isValidTag(URI uri) {
    if (uri == null)
      return false;

    if (uri.toString().equals(TAG_PREFIX) || uri.toString().startsWith(TAG_PREFIX + ID_DELEMETER))
      return true;

    return false;
  }

  public static final boolean isValidDataset(URI uri) {
    if (uri == null)
      return false;

    if (uri.toString().equals(DATASET_PREFIX) || uri.toString().startsWith(DATASET_PREFIX + ID_DELEMETER))
      return true;

    return false;
  }
}
