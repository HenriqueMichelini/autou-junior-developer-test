import TextField from "./form-section/text-field/TextField";

type ContainerParts = {
  submitContainer: SubmitContainer(),

}

function Container() {
  return <div className="flex flex-row gap-16 bg-surface rounded-sm"></div>;
}

export default Container;
