package com.non.nitrixtest.di;

import com.non.nitrixtest.dao.MovieDao;
import com.non.nitrixtest.database.AppDatabase;
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
public final class AppModule_ProvideMovieDaoFactory implements Factory<MovieDao> {
  private final Provider<AppDatabase> databaseProvider;

  public AppModule_ProvideMovieDaoFactory(Provider<AppDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public MovieDao get() {
    return provideMovieDao(databaseProvider.get());
  }

  public static AppModule_ProvideMovieDaoFactory create(Provider<AppDatabase> databaseProvider) {
    return new AppModule_ProvideMovieDaoFactory(databaseProvider);
  }

  public static MovieDao provideMovieDao(AppDatabase database) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideMovieDao(database));
  }
}
