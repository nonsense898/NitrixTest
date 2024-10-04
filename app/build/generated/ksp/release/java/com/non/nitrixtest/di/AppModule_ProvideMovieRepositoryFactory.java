package com.non.nitrixtest.di;

import com.google.gson.Gson;
import com.non.nitrixtest.dao.MovieDao;
import com.non.nitrixtest.repository.MovieRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class AppModule_ProvideMovieRepositoryFactory implements Factory<MovieRepository> {
  private final Provider<Gson> gsonProvider;

  private final Provider<MovieDao> movieDaoProvider;

  public AppModule_ProvideMovieRepositoryFactory(Provider<Gson> gsonProvider,
      Provider<MovieDao> movieDaoProvider) {
    this.gsonProvider = gsonProvider;
    this.movieDaoProvider = movieDaoProvider;
  }

  @Override
  public MovieRepository get() {
    return provideMovieRepository(gsonProvider.get(), movieDaoProvider.get());
  }

  public static AppModule_ProvideMovieRepositoryFactory create(Provider<Gson> gsonProvider,
      Provider<MovieDao> movieDaoProvider) {
    return new AppModule_ProvideMovieRepositoryFactory(gsonProvider, movieDaoProvider);
  }

  public static MovieRepository provideMovieRepository(Gson gson, MovieDao movieDao) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideMovieRepository(gson, movieDao));
  }
}
