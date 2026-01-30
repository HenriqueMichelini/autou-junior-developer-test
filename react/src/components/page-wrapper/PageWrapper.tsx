import Header from "./header/header";

function PageWrapper() {
  return (
    <div className="flex flex-col bg-surface">
      <Header title="Email" subtitle="subtitle" />
    </div>
  );
}

export default PageWrapper;
