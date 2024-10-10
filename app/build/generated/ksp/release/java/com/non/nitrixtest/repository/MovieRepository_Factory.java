package com.non.nitrixtest.repository;

import com.non.nitrixtest.dao.ApiService;
import com.non.nitrixtest.dao.MovieDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class MovieRepository_Factory implements Factory<MovieRepository> {
  private final Provider<ApiService> apiServiceProvider;

  private final Provider<MovieDao> movieDaoProvider;

  public MovieRepository_Factory(Provider<ApiService> apiServiceProvider,
      Provider<MovieDao> movieDaoProvider) {
    this.apiServiceProvider = apiServiceProvider;
    this.movieDaoProvider = movieDaoProvider;
  }

  @Override
  public MovieRepository get() {
    return newInstance(apiServiceProvider.get(), movieDaoProvider.get());
  }

  public static MovieRepository_Factory create(Provider<ApiService> apiServiceProvider,
      Provider<MovieDao> movieDaoProvider) {
    return new MovieRepository_Factory(apiServiceProvider, movieDaoProvider);
  }

  public static MovieRepository newInstance(ApiService apiService, MovieDao movieDao) {
    return new MovieRepository(apiService, movieDao);
  }
}
