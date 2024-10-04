package com.non.nitrixtest.viewmodel;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation"
})
public final class NetworkViewModel_Factory implements Factory<NetworkViewModel> {
  @Override
  public NetworkViewModel get() {
    return newInstance();
  }

  public static NetworkViewModel_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static NetworkViewModel newInstance() {
    return new NetworkViewModel();
  }

  private static final class InstanceHolder {
    private static final NetworkViewModel_Factory INSTANCE = new NetworkViewModel_Factory();
  }
}
