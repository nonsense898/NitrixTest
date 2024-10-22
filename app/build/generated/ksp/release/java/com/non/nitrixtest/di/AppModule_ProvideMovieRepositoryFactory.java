package com.non.nitrixtest.di;

import com.non.nitrixtest.network.ApiService;
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
  private final Provider<MovieDao> movieDaoProvider;

  private final Provider<ApiService> apiServiceProvider;

  public AppModule_ProvideMovieRepositoryFactory(Provider<MovieDao> movieDaoProvider,
      Provider<ApiService> apiServiceProvider) {
    this.movieDaoProvider = movieDaoProvider;
    this.apiServiceProvider = apiServiceProvider;
  }

  @Override
  public MovieRepository get() {
    return provideMovieRepository(movieDaoProvider.get(), apiServiceProvider.get());
  }

  public static AppModule_ProvideMovieRepositoryFactory create(Provider<MovieDao> movieDaoProvider,
      Provider<ApiService> apiServiceProvider) {
    return new AppModule_ProvideMovieRepositoryFactory(movieDaoProvider, apiServiceProvider);
  }

  public static MovieRepository provideMovieRepository(MovieDao movieDao, ApiService apiService) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideMovieRepository(movieDao, apiService));
  }
}
