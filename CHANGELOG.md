# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/)
and this project adheres to [Semantic Versioning](http://semver.org/spec/v2.0.0.html).

## [3.0.0] 2021-04-14

### Added
- Add SavedStateHandle support, this includes:
  - New constructors to InjectedViewModelProvider to pass the SavedStateRegistry
  - New get overloads to construct instances using the SavedStateHandle

### Changed
- Deprecated old ktx `viewModel` methods in favor of `viewModels` methods to match androidx. These
  new methods always support SavedStateHandle.
- There's a new `injectedvmprovider-fragment-ktx` artifact that you must include if you were using
  these methods on in fragments.

### Deprecated
- Deprecated InjectedViewModelProvider.of, you can use the constructors instead.

### Removed
- Removed deprecated methods

## [2.2.1] 2020-04-19

### Fixed
- Use the same default key as ViewModelProvider so the same ViewModel can be access from both a
ViewModelProvider and an InjectedViewModelProvider.

## [2.2.0] 2019-05-14

### Added
- Add get overload that takes a viewModel class to allow using a lambda
as your provider.
- Add viewModel lazy delegate functions for nice init in kotlin

### Deprecated
- Deprecated ViewModelProviders and the -extensions package, you can use
ViewModelProvider instead.

## [2.1.1] 2019-04-18

### Changed
-  Remove usage of deprecated ViewModelStores

## [2.1.0] 2019-04-15
- Androidx version of 1.1.0

## [1.1.0] 2019-04-15

### Added
- Add additional methods to support custom factories.

## [2.0] 2018-07-28
- Initial androidx version

## [1.0] 2018-07-28
- Initial version