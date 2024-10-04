package com.non.nitrixtest.repository;

import com.google.gson.Gson;
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
  private final Provider<Gson> gsonProvider;

  private final Provider<MovieDao> movieDaoProvider;

  public MovieRepository_Factory(Provider<Gson> gsonProvider, Provider<MovieDao> movieDaoProvider) {
    this.gsonProvider = gsonProvider;
    this.movieDaoProvider = movieDaoProvider;
  }

  @Override
  public MovieRepository get() {
    return newInstance(gsonProvider.get(), movieDaoProvider.get());
  }

  public static MovieRepository_Factory create(Provider<Gson> gsonProvider,
      Provider<MovieDao> movieDaoProvider) {
    return new MovieRepository_Factory(gsonProvider, movieDaoProvider);
  }

  public static MovieRepository newInstance(Gson gson, MovieDao movieDao) {
    return new MovieRepository(gson, movieDao);
  }
}
