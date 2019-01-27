Name:       databasepropertiesstorage
Version:    @@VERSION@@
Release:    1
Summary:    Store grouped name/value pairs in database

License:    GPL-3.0+
URL:        http://www.nieslony.site/dbps
Source0:    %{name}-%{version}.tar.gz

BuildRoot:  %{_tmppath}/%{name}-%{version}-%{release}-root
BuildRequires:  ant
BuildArch:  noarch

%if 0%{?fedora}
BuildRequires:  java-1.8.0-openjdk-devel lua
%endif
%if 0%{?centos_version}
BuildRequires:  java-1.8.0-openjdk-devel 
%endif
%if 0%{?suse_version}
BuildRequires:  java-1_8_0-openjdk-devel 
%endif

%description
Stores grouped name/value pairs in a PostgrSQL database

%prep
%setup

%build
ant dist -Droot.dir=%{_builddir}/%{name}-%{version}

%install
mkdir -vp %{buildroot}/usr/share/java
install dist/%{name}-%{version}.jar %{buildroot}/usr/share/java

pushd %{buildroot}/usr/share/java
ln -sv %{name}-%{version}.jar %{name}.jar
popd

mkdir -pv %{buildroot}/%_defaultdocdir/%{name}
install COPYING-GPL3        %{buildroot}/%_defaultdocdir/%{name}

%files
%attr(0644, root, root) /usr/share/java/*
%attr(0755, root, root) %_defaultdocdir/%{name}
%attr(0644, root, root) %_defaultdocdir/%{name}/*

%changelog
* Sun Oct 22 2017 Claas Nieslony <claas@nieslony.at> 0.1.0
- Initial version
